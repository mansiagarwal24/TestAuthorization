//package com.ttn.bootcamp.project.bootcampproject.Bootstrap;
//
//public class Bootstrap implements ApplicationListener {
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    RoleRepo roleRepo;
//
//    @Autowired
//    UserRepo userRepo;
//
//    @Value("${admin.email}")
//    String email;
//
//    String name;
//
//    @Value("${admin.password}")
//    CharSequence password;
//    @Override
//    public void onApplicationEvent(ApplicationEvent applicationEvent){
//        if(!roleRepo.existsByAuthority(Authority.ADMIN)){
//            Role role = new Role();
//            role.setAuthority(Authority.ADMIN);
//            roleRepo.save(role);
//        }
//        if(!roleRepo.existsByAuthority(Authority.CUSTOMER)){
//            Role role = new Role();
//            role.setAuthority(Authority.CUSTOMER);
//            roleRepo.save(role);
//        }
//
//        if(!roleRepo.existsByAuthority(Authority.SELLER)){
//            Role role = new Role();
//            role.setAuthority(Authority.SELLER);
//            roleRepo.save(role);
//        }
//        if(!userRepo.existsByEmail(email)){
//            User user = new User();
//            user.setEmail(email);
//            user.setFirstName(name);
//            user.setPassword(passwordEncoder.encode(password));
//            user.setIsActive(Boolean.TRUE);
//            user.setPasswordUpdateDate(LocalDate.now());
//            Optional<Role> byAuthority = roleRepo.findByAuthority(Authority.ADMIN);
//            user.setRole(byAuthority.stream().toList());
//            userRepo.save(user);
//        }
//    }
//}
