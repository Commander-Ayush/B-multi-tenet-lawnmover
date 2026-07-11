package com.growthmul.app.lawnmover_fs.config;

import com.growthmul.app.lawnmover_fs.entity.AdminUser;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.repository.AdminUserRepository;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Local-dev convenience only. Runs once on startup; does nothing at all if
 * any Company row already exists (so this never touches a real database
 * with real client data — it only fires against a genuinely empty one,
 * e.g. the first time you run this app on your machine).
 *
 * Seeds ONE demo company + admin login so the storefront/admin-panel work
 * immediately against localhost without any manual SQL.
 *
 * IMPORTANT: the seeded domain is "localhost:5500" — adjust to match
 * whatever port you actually serve storefront/ on (e.g. VS Code Live
 * Server, `python3 -m http.server`). The browser's Origin header includes
 * the port, and the domain column has to match it exactly. Opening the
 * HTML files directly via file:// won't work — Origin is "null" in that
 * case, not a real domain. Serve them over a local HTTP server.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired private CompanyRepo companyRepo;
    @Autowired private AdminUserRepository adminUserRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    private static final String DEV_DOMAIN = "127.0.0.1:5500";
    private static final String DEV_ADMIN_EMAIL = "admin@demo.local";
    private static final String DEV_ADMIN_PASSWORD = "ChangeMe123!"; // dev only — change immediately for any real use

    @Override
    public void run(ApplicationArguments args) {
        System.out.println((passwordEncoder.encode(DEV_ADMIN_PASSWORD)));
        if (companyRepo.count() > 0) return; // real data already exists — never touch it

        Company demo = new Company();
        demo.setName("GreenCut Lawn Services");
        demo.setCity("Austin");
        demo.setFoundedYear(2015);
        demo.setYardsServed(4800);
        demo.setYearsExperience(11);
        demo.setPhone(8004736288L);
        demo.setEmail("hello@greencut.com");
        demo.setDomain(DEV_DOMAIN);
//        companyRepo.save(demo);

        AdminUser admin = new AdminUser();
        admin.setEmail(DEV_ADMIN_EMAIL);
        admin.setPasswordHash(passwordEncoder.encode(DEV_ADMIN_PASSWORD));
        admin.setCompany(demo);
//        adminUserRepo.save(admin);

        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" Seeded a demo company for local dev:");
        System.out.println("   domain:   " + DEV_DOMAIN + "  (must match storefront's Origin exactly)");
        System.out.println("   admin login: " + DEV_ADMIN_EMAIL + " / " + DEV_ADMIN_PASSWORD);
        System.out.println("─────────────────────────────────────────────────────");
    }
}
