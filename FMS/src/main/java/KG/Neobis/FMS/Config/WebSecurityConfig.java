package KG.Neobis.FMS.Config;

import KG.Neobis.FMS.Services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder myBCryptPasswordEncoder;
    private final JwtOutils jwtOutils;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder myBCryptPasswordEncoder, JwtOutils jwtOutils, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.myBCryptPasswordEncoder = myBCryptPasswordEncoder;
        this.jwtOutils = jwtOutils;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()


                .antMatchers("/admin/**").hasAuthority("АДМИН")

                .antMatchers("/resources/**").permitAll()
                .antMatchers(HttpMethod.GET,"/cash_accounts/**").authenticated()
                .antMatchers(HttpMethod.POST,"/cash_accounts").hasAuthority("Добавление_счета")
                .antMatchers(HttpMethod.PUT,"/cash_accounts/**").hasAuthority("Изменение_данных_счета")
                .antMatchers(HttpMethod.GET, "/transactions/**").authenticated()
                .antMatchers(HttpMethod.POST, "/income_transactions").hasAuthority("Добавление_транзакции")
                .antMatchers(HttpMethod.POST, "/expense_transactions").hasAuthority("Добавление_транзакции")
                .antMatchers(HttpMethod.POST, "/transfer_transactions").hasAuthority("Добавление_транзакции")
                .antMatchers(HttpMethod.DELETE, "/delete_transaction/**").hasAuthority("Удаление_транзакции")
                .antMatchers(HttpMethod.POST, "/contractors").hasAuthority("Добавление/изменение_контрагентов")
                .antMatchers(HttpMethod.PUT, "/contractors/**").hasAuthority("Добавление/изменение_контрагентов")
                .antMatchers(HttpMethod.POST,"/expenses_categories").hasAuthority("Добавление/изменение_категории")
                .antMatchers(HttpMethod.PUT,"/expenses_categories/**").hasAuthority("Добавление/изменение_категории")
                .antMatchers(HttpMethod.POST,"/incomes_categories").hasAuthority("Добавление/изменение_категории")
                .antMatchers(HttpMethod.PUT,"/incomes_categories/**").hasAuthority("Добавление/изменение_категории")
                .antMatchers(HttpMethod.POST,"/projects/**").hasAuthority("Добавление/изменение_проектов")
                .antMatchers(HttpMethod.PUT,"/projects/**").hasAuthority("Добавление/изменение_проектов")
                .antMatchers(HttpMethod.POST, "/login").permitAll()

                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")

                .and()

                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()

                .and()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtOutils))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtOutils))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**","/API");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(myBCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}
