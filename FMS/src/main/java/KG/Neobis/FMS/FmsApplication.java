package KG.Neobis.FMS;

import KG.Neobis.FMS.Entities.Users.AppUsers;
import KG.Neobis.FMS.Entities.Users.Privilege;
import KG.Neobis.FMS.Entities.Users.Roles;
import KG.Neobis.FMS.Repositories.AppUserRepository;
import KG.Neobis.FMS.Repositories.PrivilegeRepository;
import KG.Neobis.FMS.Repositories.RoleRepository;
import KG.Neobis.FMS.Services.*;
import io.swagger.annotations.Api;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableSwagger2
@SpringBootApplication
public class FmsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FmsApplication.class, args);
	}

	private final AppUserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PrivilegeRepository privilegeRepository;

	public FmsApplication(AppUserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(PathSelectors.any()).build().pathMapping("/")
				.apiInfo(apiInfo()).useDefaultResponseMessages(false);
	}

	@Bean
	public ApiInfo apiInfo() {
		final ApiInfoBuilder builder = new ApiInfoBuilder();
		builder.title("My Application API through Swagger UI").version("1.0").license("(C) Copyright Test")
				.description("List of all the APIs of My Application App through Swagger UI");
		return builder.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		Roles roles = new Roles();
		if (!roleRepository.existsByName("СУПЕР АДМИН")){
			roles = roleRepository.save(new Roles("СУПЕР АДМИН",createPrivileges()));
		}
		if (!userRepository.existsByEmail("finance.mng5@gmail.com")){
			userRepository.save(new AppUsers("finance.mng5","$2y$10$naaDBuTsBbFce5wB4ulsqe5zqMUVyqHuREAxmYFIYLZnl8u1/C7x6",
					"finance.mng5@gmail.com", Collections.singletonList(roles)));
		}
	}

	private ArrayList<Privilege> createPrivileges(){
		ArrayList<Privilege> privileges = new ArrayList<>();
		if (!privilegeRepository.existsByAuthority("АДМИН")){
			privileges.add(privilegeRepository.save(new Privilege("АДМИН")));
		}
		if (!privilegeRepository.existsByAuthority("Добавление_счета")){
			privileges.add(privilegeRepository.save(new Privilege("Добавление_счета")));
		}
		if (!privilegeRepository.existsByAuthority("Изменение_данных_счета")){
			privileges.add(privilegeRepository.save(new Privilege("Изменение_данных_счета")));
		}
		if (!privilegeRepository.existsByAuthority("Добавление_транзакции")){
			privileges.add(privilegeRepository.save(new Privilege("Добавление_транзакции")));
		}
		if (!privilegeRepository.existsByAuthority("Удаление_транзакции")){
			privileges.add(privilegeRepository.save(new Privilege("Удаление_транзакции")));
		}
		if (!privilegeRepository.existsByAuthority("Добавление/изменение_контрагентов")){
			privileges.add(privilegeRepository.save(new Privilege("Добавление/изменение_контрагентов")));
		}
		if (!privilegeRepository.existsByAuthority("Добавление/изменение_категории")){
			privileges.add(privilegeRepository.save(new Privilege("Добавление/изменение_категории")));
		}
		if (!privilegeRepository.existsByAuthority("Добавление/изменение_проектов")){
			privileges.add(privilegeRepository.save(new Privilege("Добавление/изменение_проектов")));
		}
		return privileges;
	}
}
