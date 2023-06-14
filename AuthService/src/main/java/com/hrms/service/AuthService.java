package com.hrms.service;

import com.hrms.dto.request.*;
import com.hrms.dto.response.LoginResponseDto;
import com.hrms.exception.AuthManagerException;
import com.hrms.exception.ErrorType;
import com.hrms.manager.ICompanyManager;
import com.hrms.manager.IUserManager;
import com.hrms.mapper.IAuthMapper;
import com.hrms.rabbitmq.model.ForgotPasswordMailModel;
import com.hrms.rabbitmq.producer.ForgotPasswordMailProducer;
import com.hrms.rabbitmq.producer.MailProducer;
import com.hrms.repository.IAuthRepository;
import com.hrms.repository.entity.Auth;
import com.hrms.repository.entity.enums.EStatus;
import com.hrms.utility.CodeGenerator;
import com.hrms.utility.JwtTokenProvider;
import com.hrms.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserManager userManager;
    private final JwtTokenProvider tokenProvider;
    private final RoleService roleService;
    private final MailProducer mailProducer;
    private final ForgotPasswordMailProducer forgotPasswordMailProducer;
    private final ICompanyManager companyManager;

    public AuthService(IAuthRepository authRepository, PasswordEncoder passwordEncoder,
                       IUserManager userManager, JwtTokenProvider tokenProvider,
                       RoleService roleService, MailProducer mailProducer,
                       ForgotPasswordMailProducer forgotPasswordMailProducer, ICompanyManager companyManager) {
        super(authRepository);
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.userManager = userManager;
        this.tokenProvider = tokenProvider;
        this.roleService = roleService;
        this.mailProducer = mailProducer;
        this.forgotPasswordMailProducer = forgotPasswordMailProducer;
        this.companyManager = companyManager;
    }

    public Boolean register(RegisterRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(dto.getEmail());
        if (optionalAuth.isPresent())
            throw new AuthManagerException(ErrorType.EMAIL_DUPLICATE);
        Auth auth = IAuthMapper.INSTANCE.fromRegisterRequestDtoToAuth(dto);
        List<Long> roleList = new ArrayList<>();
        Long companyId = null;
        if (dto.getCompanyName()==null){
            roleList.add(4L);
            auth.setRoleIds(roleList);
            auth.setEStatus(EStatus.ACTIVE);
        }
        else {
            roleList.add(2L);
            roleList.add(3L);
            auth.setRoleIds(roleList);
             companyId = companyManager.getCompanyIdWithCompanyName(dto.getCompanyName()).getBody();
            //TODO: CompanyName daha önce olup olmadığı varsa nasıl yönetileceği
        }
        if (dto.getPassword().equals(dto.getRepassword())) {
            auth.setActivateCode(CodeGenerator.generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            Auth savedAuth = save(auth);
            FromAuthToUserProfileCreateUserRequestDto createUserRequestDto = IAuthMapper.INSTANCE.fromAuthToCreateUserRequestDto(savedAuth);
            createUserRequestDto.setCompanyId(companyId);
            userManager.createUser(createUserRequestDto);
            if(auth.getRoleIds().contains(2L)){
                mailProducer.sendActivationLink(IAuthMapper.INSTANCE.fromAuthToRegisterMailModel(savedAuth));
            }
            return true;
        } else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }

    }

    public LoginResponseDto login(LoginRequestDto dto) {
        System.out.println("login çalıştı");
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(dto.getEmail());
        if (optionalAuth.isEmpty() || !passwordEncoder.matches(dto.getPassword(), optionalAuth.get().getPassword()))
            throw new RuntimeException("kullanıcı adı veya şifre hatalı");
        else if (!optionalAuth.get().getEStatus().equals(EStatus.ACTIVE))
            throw new RuntimeException("Aktif olmalısınız");
        List<String> roles = new ArrayList<>();
        optionalAuth.get().getRoleIds().forEach(roleId -> {
            roles.add(roleService.getRoleNames(roleId)); // burada patlıyor
        });
        System.out.println("roller aşağıda");
        System.out.println(roles);
        Optional<String> token = tokenProvider.createToken(optionalAuth.get().getAuthId(), roles);
        /*return token.orElseThrow(() -> {
            throw new RuntimeException("token üretilemedi");
        });*/
        System.out.println("token aşağıda");
        System.out.println(tokenProvider.getIdFromToken(token.get()));
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .token(token.get())
                .roles(tokenProvider.getRolesFromToken(token.get()))
                .build();
        System.out.println(loginResponseDto.getRoles());
        return loginResponseDto;
    }

    public Boolean activateStatus(String token) {
        Optional<Long> authId = tokenProvider.getIdFromToken(token);
        if (authId.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        Optional<Auth> optionalAuth = findById(authId.get());
        if (optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        if (optionalAuth.get().getRoleIds().contains(2L)) {
            optionalAuth.get().setEStatus(EStatus.INACTIVE);
            update(optionalAuth.get());
        }
        return true;
    }

    public Boolean doActivateManager(FromUserProfileToAuthForActivateManager dto) {
        Optional<Auth> auth = findById(dto.getAuthId());
        if (auth.isEmpty()) throw new RuntimeException("Kullanıcı yok");
        auth.get().setEStatus(dto.getStatus());
        update(auth.get());
        return true;
    }

    public Boolean forgotPasswordMail(String email){
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(email);
        if(optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        if(optionalAuth.get().getEStatus().equals(EStatus.ACTIVE)){
            ForgotPasswordMailModel model = ForgotPasswordMailModel.builder()
                    .authId(optionalAuth.get().getAuthId())
                    .email(email)
                    .build();
            forgotPasswordMailProducer.sendForgotPasswordMail(model);
        }
        return true;
    }

    public Boolean changeForgotPassword(){
        return true;
    }

    public Boolean forgotPassword(ForgotPasswordRequestDto dto) {
        Optional<Auth> optionalAuth = findById(tokenProvider.getIdFromToken(dto.getToken()).get());
        if(optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        if(!dto.getPassword().equals(dto.getRepassword()))
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        optionalAuth.get().setPassword(passwordEncoder.encode(dto.getPassword()));
        update(optionalAuth.get());
        return true;
    }

    public Long createEmployee(FromUserProfileToAuthAddEmployeeRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Auth auth = save(IAuthMapper.INSTANCE.fromFromUserProfileToAuthAddEmployeeRequestDtoToAuth(dto));
        auth.setEStatus(EStatus.ACTIVE);
        // role eklenmeli
        List<Long> roleList = new ArrayList<>();
        roleList.add(3L);
        auth.setRoleIds(roleList);
        update(auth);
        return auth.getAuthId();
    }
    public Boolean updateAuth(String token,UserUpdateRequestDto dto ) {
        Optional<Long> authId=tokenProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> auth=findById(authId.get());
        if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        update(IAuthMapper.INSTANCE.fromUserUpdateRequestDtotoAuth(auth.get(),dto));
        return  true;
    }

    public Boolean deleteAuth(Long authId) {
        Optional<Auth> optionalAuth = findById(authId);
        if(optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        optionalAuth.get().setEStatus(EStatus.DELETED);
        update(optionalAuth.get());
        return true;
    }
}
