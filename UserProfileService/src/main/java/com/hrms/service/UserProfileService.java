package com.hrms.service;

import com.hrms.dto.request.*;
import com.hrms.dto.response.AddAddressResponseDto;
import com.hrms.dto.response.GetNameByIdResponseDto;
import com.hrms.dto.response.GetOffDaysResponseDto;
import com.hrms.exception.ErrorType;
import com.hrms.exception.UserProfileManagerException;
import com.hrms.manager.IAuthManager;
import com.hrms.manager.ICompanyManagersManager;
import com.hrms.mapper.IUserProfileMapper;
import com.hrms.rabbitmq.model.AddEmployeeMailModel;
import com.hrms.rabbitmq.producer.MailProducer;
import com.hrms.repository.entity.UserProfile;
import com.hrms.repository.IUserProfileRepository;
import com.hrms.repository.entity.enums.EStatus;
import com.hrms.utility.JwtTokenProvider;
import com.hrms.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {

    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final IAuthManager authManager;
    private final ICompanyManagersManager companyManagersManager;
    private final MailProducer mailProducer;
    private final AddressService addressService;

    public UserProfileService(IUserProfileRepository userProfileRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, IAuthManager authManager, ICompanyManagersManager companyManagersManager, MailProducer mailProducer, AddressService addressService) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.companyManagersManager = companyManagersManager;
        this.mailProducer = mailProducer;
        this.addressService = addressService;
    }

    public Boolean createUser(CreateUserRequestDto dto) {
        UserProfile userProfile = IUserProfileMapper.INSTANCE.createUser(dto);
        save(userProfile);
        return true;
    }

    public Boolean doActiveManager(String token, Long authId) {
        List<String> authRoles = jwtTokenProvider.getRolesFromToken(token);
        List<UserProfile> managers = userProfileRepository.findAllByStatus(EStatus.INACTIVE);
        for (String authRole : authRoles) {
            if (authRole.equals("ADMIN")) {
                managers.forEach(x -> {
                    if (x.getAuthId().equals(authId)) {
                        x.setStatus(EStatus.ACTIVE);
                        update(x);
                        authManager.doActivateManager(FromUserProfileToAuthForActivateManager.builder()
                                .authId(authId)
                                .status(x.getStatus())
                                .build());
                    }
                });
            }
        }
        String role= "ADMIN";
        if (authRoles.contains(role)) {
            managers.forEach(x -> {
                if (x.getAuthId().equals(authId)) {
                    companyManagersManager.createCompanyManager(CreateCompanyManagerRequestDto.builder()
                            .userId(x.getUserId())
                            .companyId(x.getCompanyId())
                            .name(x.getName())
                            .surname(x.getSurname())
                            .build());
                }
            });
        }
        return true;
    }

    public Boolean addEmployee(String token, AddEmployeeRequestDto dto){
        List<String> roles = jwtTokenProvider.getRolesFromToken(token);
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        Optional<UserProfile> userProfileManager = userProfileRepository.findByAuthId(authId.get());
        if(userProfileManager.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        if(!roles.contains("MANAGER")){
            throw new UserProfileManagerException(ErrorType.NOT_AUTHORIZED);
        }else{
            UserProfile userProfile = IUserProfileMapper.INSTANCE.fromAddEmployeeRequestDtoToUserProfile(dto);
            FromUserProfileToAuthAddEmployeeRequestDto addEmployeeRequestDto =
                    IUserProfileMapper.INSTANCE.fromUserProfileToFromUserProfileToAuthAddEmployeeRequestDto(userProfile);
            AddEmployeeMailModel model = IUserProfileMapper.INSTANCE.fromAddEmployeeRequestDtoToAddEmployeeMailModel(dto);
            Long fromAuthId = authManager.createEmployee(addEmployeeRequestDto).getBody();
            userProfile.setPassword(passwordEncoder.encode(dto.getPassword()));
            userProfile.setAuthId(fromAuthId);
            userProfile.setCompanyId(userProfileManager.get().getCompanyId());
            userProfile.setStatus(EStatus.ACTIVE);
            save(userProfile);
            mailProducer.sendEmployeeInfo(model);
            return true;
        }
    }

  /*  public Boolean deleteEmployee(String employeeId, String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        System.out.println(userProfile);
        if (userProfile.get().getRole().equals(ERole.DIRECTORY)) {
            Optional<UserProfile> employee = findById(employeeId);
            if (userProfile.get().getCompanyName().toLowerCase().equals(employee.get().getCompanyName().toLowerCase())) {
                if (employee.isEmpty()) {
                    throw new UserManagerException(ErrorType.EMPLOYEE_NOT_FOUND);
                }
                employee.get().setStatus(EStatus.DELETED);
                update(employee.get());
                return true;
            } else {
                throw new UserManagerException(ErrorType.DIRECTORY_ERROR);
            }
        }
        throw new UserManagerException(ErrorType.ROLE_ERROR);
    }

    public Boolean activateDirector(String token, Long directorId) {
        Optional<String> adminRole = jwtTokenProvider.getRoleFromToken(token);
        Optional<Auth> director = findById(directorId);
        if (adminRole.get().isEmpty())
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        if (director.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (director.get().getIsActive()== false) {
            if (director.get().getCompanyName().isEmpty()) {
                director.get().setStatus(EStatus.BANNED);
            }
            director.get().setIsActive(true);
            save(director.get());
            userManager.activateDirector(directorId);
        }
        return true;
    }

    */
/*  public List<String> findByCompanyName(Long companyId) {
      List<UserProfile> userProfileList = userProfileRepository.findAllByCompanyId(companyId);
        *//*if (userProfileList.size()==0) {
        // todo if controlü çalışmıyor kontrol edilmesi ve refactor edilmesi lazım

            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }*//*
        List<String> results = new ArrayList<>();
        for(UserProfile user: userProfileList){
            // todo bu kontrol admin onay metodundan sonra eklenecek
            // user.getIsActivatedByAdmin().equals(true) &&
            if (user.getRole().equals(ERole.DIRECTORY)) {
                String fullName = user.getName()+" "+user.getSurname();
                results.add(fullName);
            }
        }
        return results;
    }*/
  public Boolean updateUserProfile(String token, UserUpdateRequestDto dto) {
      Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
      if (authId.isEmpty()) throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
      Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId.get());
      if (userProfile.isEmpty()) throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
      update(IUserProfileMapper.INSTANCE.fromUpdateUserProfileToUserProfile(userProfile.get(), dto));
      authManager.updateUserProfile(token, dto);
      return true;
  }

    public AddAddressResponseDto userProfileAddAddressId(String token, AddAddressRequestDto dto) {
        AddAddressResponseDto responseDto= addressService.addAddress(token, dto);
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty()) throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        if (userProfile.get().getAddressId()==null){
            userProfile.get().setAddressId(addressService.getAddressId(authId.get()).getAddressId());
        }
        IUserProfileMapper.INSTANCE.fromUserProfileToAddAddressRequestDto(userProfile.get(), dto);
        update(userProfile.get());
        return responseDto;
    }
    public GetNameByIdResponseDto findNameById(String userId) {
        return IUserProfileMapper.INSTANCE.toGetNameDto(findById(userId).get());
    }
    public Long takeOffDay(TakeOffDayRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty()) throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        userProfile.get().getDaysOffList().addAll(dto.getDaysOffList());// front end de gelen tatil günlerini ekledik
        if (userProfile.get().getOffDaysRemaining() < dto.getDaysOffList().size()) {
            throw new UserProfileManagerException(ErrorType.OUT_OFF_HOLIDAYS);
        }
        userProfile.get().setOffDaysRemaining(userProfile.get().getOffDaysRemaining()-dto.getDaysOffList().size());
        update(userProfile.get());
        return userProfile.get().getOffDaysRemaining();
    }

    /**
     * kullanıcının aldığı izin günleri listesi (String)
     * @param token personelin kendi tokenı verilecek
     * @return kullanıcının aldığı izin günleri listesi (String)
     */
    public GetOffDaysResponseDto getOffDays(String token){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty()) throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return GetOffDaysResponseDto.builder().daysOffList(userProfile.get().getDaysOffList()).build();
    }
    public UserProfile addOffDays(AddOffDaysRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(dto.getAuthId());
        if (userProfile.isEmpty()) throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        List<String> roles = jwtTokenProvider.getRolesFromToken(dto.getToken());
        if (!roles.stream().anyMatch(x-> x.equals("MANAGER"))) {
            throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
        userProfile.get().setOffDaysRemaining(userProfile.get().getOffDaysRemaining()+dto.getOffDays());
        return update(userProfile.get());
    }

    /**
     * metot maaş günlerini ayın 1 olarak base alıyor ve ay 25 den büyükse eğer true değer dönüyor bu da front end
     * de true değer olduğunda şu şekilde bir yazının visibility sini true yapaak ve şu yazacak maaşa 3 gün 2 gün kaldı falan
     * @return
     */
    public Boolean payDayComes(){
        System.out.println(LocalDate.now());
        String timeNow= LocalDate.now().toString();
        int length = timeNow.length();
        String dayNow = timeNow.substring(length-2);
        System.out.println(dayNow); // ayın 13 olarak geldi
        if (Integer.parseInt(dayNow)>=25 ) {
            return true; // maaş ödemesine bu kadar kaldı
        }
        return false;
    }
    public UserProfile addSalaryIncrease(AddSalaryIncreaseRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> managerUser = userProfileRepository.findByAuthId(authId.get());
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(dto.getAuthId());
        if (!userProfile.get().getCompanyId().equals(managerUser.get().getCompanyId())) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_AT_SAME_COMPANY);
        }
        if (userProfile.isEmpty()) throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        String oldSalary = userProfile.get().getSalary();
        int oldSalaryd = Integer.parseInt(oldSalary);
        int newSalary = (int) (oldSalaryd * (1+Double.parseDouble(dto.getSalaryIncreaseRate())));
        userProfile.get().setSalary(String.valueOf(newSalary));
        return update(userProfile.get());
    }
    public UserProfile updateSalary(UpdateSalaryRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> managerUser = userProfileRepository.findByAuthId(authId.get());
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(dto.getAuthId());
        if (!userProfile.get().getCompanyId().equals(managerUser.get().getCompanyId())) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_AT_SAME_COMPANY);
        }
        if (userProfile.isEmpty()) throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        userProfile.get().setSalary(dto.getNewSalary());
        return update(userProfile.get());
    }

    public Boolean deleteEmployee(String token, String email){
        List<String> roles = jwtTokenProvider.getRolesFromToken(token);
        if(!roles.contains("MANAGER"))
            throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByEmail(email);
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        optionalUserProfile.get().setStatus(EStatus.DELETED);
        update(optionalUserProfile.get());
        authManager.deleteAuth(optionalUserProfile.get().getAuthId());
        return true;
    }
}
