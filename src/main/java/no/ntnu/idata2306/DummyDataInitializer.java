package no.ntnu.idata2306;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.enums.OrderStatusEnum;
import no.ntnu.idata2306.enums.PaymentMethodEnum;
import no.ntnu.idata2306.model.*;
import no.ntnu.idata2306.model.course.details.*;
import no.ntnu.idata2306.model.course.Course;
import no.ntnu.idata2306.model.course.details.Currency;
import no.ntnu.idata2306.model.payment.OrderStatus;
import no.ntnu.idata2306.model.payment.PaymentMethod;
import no.ntnu.idata2306.repository.course.CourseEnrollmentsRepository;
import no.ntnu.idata2306.repository.course.details.*;
import no.ntnu.idata2306.repository.course.CourseRepository;
import no.ntnu.idata2306.repository.*;
import no.ntnu.idata2306.repository.course.details.RelatedCertificateRepository;
import no.ntnu.idata2306.repository.course.details.TopicRepository;
import no.ntnu.idata2306.repository.payment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Responsible for populating database with dummy data for testing.
 */
@Slf4j
@Component
public class DummyDataInitializer implements ApplicationListener<ApplicationEvent> {

    private static final Random RANDOM = new Random();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CreditRepository creditRepository;
    private final CurrencyRepository currencyRepository;
    private final DifficultyLevelRepository difficultyLevelRepository;
    private final HoursPerWeekRepository hoursPerWeekRepository;
    private final PasswordEncoder passwordEncoder;
    private  final RelatedCertificateRepository relatedCertificateRepository;
    private final TopicRepository topicRepository;

    private final ReviewRepository reviewRepository;

    private final OrderStatusRepository orderStatusRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final CourseEnrollmentsRepository courseEnrollmentsRepository;
    private final ProviderRepository providerRepository;

    @Autowired
    public DummyDataInitializer(UserRepository userRepository, RoleRepository roleRepository, CourseRepository courseRepository,
                                CategoryRepository categoryRepository, CreditRepository creditRepository, CurrencyRepository currencyRepository,
                                DifficultyLevelRepository difficultyLevelRepository, HoursPerWeekRepository hoursPerWeekRepository,
                                @Lazy PasswordEncoder passwordEncoder, TopicRepository topicRepository, RelatedCertificateRepository relatedCertificateRepository,
                                ReviewRepository reviewRepository, OrderStatusRepository orderStatusRepository, CourseEnrollmentsRepository courseEnrollmentsRepository,
                                OrderRepository orderRepository, PaymentRepository paymentRepository, PaymentMethodRepository paymentMethodRepository,
                                PaymentCardRepository paymentCardRepository, ProviderRepository providerRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.creditRepository = creditRepository;
        this.currencyRepository = currencyRepository;
        this.difficultyLevelRepository = difficultyLevelRepository;
        this.hoursPerWeekRepository = hoursPerWeekRepository;
        this.passwordEncoder = passwordEncoder;
        this.topicRepository = topicRepository;
        this.relatedCertificateRepository = relatedCertificateRepository;
        this.reviewRepository = reviewRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderRepository = orderRepository;
        this.courseEnrollmentsRepository = courseEnrollmentsRepository;
        this.paymentCardRepository = paymentCardRepository;
        this.paymentRepository = paymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("Importing test data...");

        if (userRepository.count() == 0 && providerRepository.count() == 0){

            // Create providers
            Provider ntnuProvider = Provider.builder()
                    .providerName("NTNU")
                    .providerUrl("https://www.ntnu.edu")
                    .providerLogoUrl("https://www.ntnu.edu/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider oracleProvider = Provider.builder()
                    .providerName("Oracle")
                    .providerUrl("https://www.oracle.com")
                    .providerLogoUrl("https://www.oracle.com/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider apacheProvider = Provider.builder()
                    .providerName("Apache Software Foundation")
                    .providerUrl("https://www.apache.org")
                    .providerLogoUrl("https://www.apache.org/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider pearsonProvider = Provider.builder()
                    .providerName("Pearson")
                    .providerUrl("https://www.pearson.com")
                    .providerLogoUrl("https://www.pearson.com/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider microsoftProvider = Provider.builder()
                    .providerName("Microsoft")
                    .providerUrl("https://www.microsoft.com")
                    .providerLogoUrl("https://www.microsoft.com/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider amazonProvider = Provider.builder()
                    .providerName("Amazon")
                    .providerUrl("https://www.amazon.com")
                    .providerLogoUrl("https://www.amazon.com/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider adobeProvider = Provider.builder()
                    .providerName("Adobe")
                    .providerUrl("https://www.adobe.com")
                    .providerLogoUrl("https://www.adobe.com/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider appleProvider = Provider.builder()
                    .providerName("Apple")
                    .providerUrl("https://www.apple.com")
                    .providerLogoUrl("https://www.apple.com/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider googleProvider = Provider.builder()
                    .providerName("Google")
                    .providerUrl("https://www.google.com")
                    .providerLogoUrl("https://www.google.com/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider biProvider = Provider.builder()
                    .providerName("Handelshøyskolen BI")
                    .providerUrl("https://www.bi.no")
                    .providerLogoUrl("https://www.bi.no/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider uioProvider = Provider.builder()
                    .providerName("University of Oslo")
                    .providerUrl("https://www.uio.no")
                    .providerLogoUrl("https://www.uio.no/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            Provider uibProvider = Provider.builder()
                    .providerName("University of Bergen")
                    .providerUrl("https://www.uib.no")
                    .providerLogoUrl("https://www.uib.no/logo.png")
                    .created(LocalDateTime.now())
                    .build();

            // Save providers
            providerRepository.save(ntnuProvider);
            providerRepository.save(oracleProvider);
            providerRepository.save(apacheProvider);
            providerRepository.save(pearsonProvider);
            providerRepository.save(microsoftProvider);
            providerRepository.save(amazonProvider);
            providerRepository.save(adobeProvider);
            providerRepository.save(appleProvider);
            providerRepository.save(googleProvider);
            providerRepository.save(biProvider);
            providerRepository.save(uioProvider);
            providerRepository.save(uibProvider);

            // Create roles and users
            Role adminRole = new Role();
            adminRole.setRole("ADMIN");

            Role userRole = new Role();
            userRole.setRole("USER");

            Role providerRole = new Role();
            providerRole.setRole("PROVIDER");

            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            roleRepository.save(providerRole);

            // Create users
            User adminUser1 = new User();
            adminUser1.setFirstName("Dave");
            adminUser1.setLastName("Davidson");
            adminUser1.setEmail("dave");
            adminUser1.setPassword(this.passwordEncoder.encode("Dangerous2024"));
            adminUser1.setCreated(LocalDateTime.now());
            adminUser1.setDeleted(false);
            adminUser1.setProvider(ntnuProvider);
            adminUser1.setRoles(new LinkedHashSet<>(Set.of(adminRole)));

            User regularUser1 = new User();
            regularUser1.setFirstName("Chuck");
            regularUser1.setLastName("Chicken");
            regularUser1.setEmail("chuck");
            regularUser1.setPassword(this.passwordEncoder.encode("Nunchucks2024"));
            regularUser1.setCreated(LocalDateTime.now());
            regularUser1.setProvider(ntnuProvider);
            regularUser1.setDeleted(false);
            regularUser1.setRoles(new LinkedHashSet<>(Set.of(userRole)));

            User adminUser2 = new User();
            adminUser2.setFirstName("Admin");
            adminUser2.setLastName("User");
            adminUser2.setEmail("admin@example.com");
            adminUser2.setPassword(this.passwordEncoder.encode("adminpass"));
            adminUser2.setCreated(LocalDateTime.now());
            adminUser2.setDeleted(false);
            adminUser2.setProvider(oracleProvider);
            adminUser2.setRoles(new LinkedHashSet<>(Set.of(adminRole)));

            User regularUser2 = new User();
            regularUser2.setFirstName("Regular");
            regularUser2.setLastName("User");
            regularUser2.setEmail("user@example.com");
            regularUser2.setPassword(this.passwordEncoder.encode("userpass"));
            regularUser2.setCreated(LocalDateTime.now());
            regularUser2.setDeleted(false);
            regularUser2.setProvider(oracleProvider);
            regularUser2.setRoles(new LinkedHashSet<>(Set.of(userRole)));

            User providerUser = new User();
            providerUser.setFirstName("Provider");
            providerUser.setLastName("User");
            providerUser.setEmail("provider@example.com");
            providerUser.setPassword(this.passwordEncoder.encode("providerpass"));
            providerUser.setCreated(LocalDateTime.now());
            providerUser.setDeleted(false);
            providerUser.setProvider(oracleProvider);
            providerUser.setRoles(new LinkedHashSet<>(Set.of(providerRole)));

            User providerUser2 = new User();
            providerUser2.setFirstName("Provider");
            providerUser2.setLastName("User");
            providerUser2.setEmail("provider2@example.com");
            providerUser2.setPassword(this.passwordEncoder.encode("providerpass"));
            providerUser2.setCreated(LocalDateTime.now());
            providerUser2.setDeleted(false);
            providerUser2.setProvider(oracleProvider);
            providerUser2.setRoles(new LinkedHashSet<>(Set.of(providerRole)));

            // Save users
            userRepository.save(adminUser1);
            userRepository.save(regularUser1);
            userRepository.save(adminUser2);
            userRepository.save(regularUser2);
            userRepository.save(providerUser);
            userRepository.save(providerUser2);

            // Create categories
            Category itCategory = new Category();
            itCategory.setCategory("Information Technologies");

            Category dmCategory = new Category();
            dmCategory.setCategory("Digital Marketing");

            Category beCategory = new Category();
            beCategory.setCategory("Business and Entrepreneurship");

            Category dsaCategory = new Category();
            dsaCategory.setCategory("Data Science and Analytics");

            categoryRepository.save(itCategory);
            categoryRepository.save(dmCategory);
            categoryRepository.save(beCategory);
            categoryRepository.save(dsaCategory);

            // Create credits

            // 0.5 - 30 credit, incremented by 0.5
            for (double i = 0.5; i <= 30; i += 0.5) {
                Credit credit = new Credit();
                credit.setCredit(BigDecimal.valueOf(i));
                creditRepository.save(credit);
            }

            Credit credit7_5 = new Credit(15, new BigDecimal("7.5"), null);
            Credit credit2 = new Credit(4, new BigDecimal("2"), null);
            Credit credit4 = new Credit(8, new BigDecimal("4"), null);
            Credit credit10 = new Credit(20, new BigDecimal("10"), null);

            // Create currencies
            Currency nok = new Currency();
            nok.setCurrency("NOK");

            Currency usd = new Currency();
            usd.setCurrency("USD");

            currencyRepository.save(nok);
            currencyRepository.save(usd);

            // Create difficulty levels
            DifficultyLevel beginner = new DifficultyLevel();
            beginner.setType("Beginner");

            DifficultyLevel intermediate = new DifficultyLevel();
            intermediate.setType("Intermediate");

            DifficultyLevel expert = new DifficultyLevel();
            expert.setType("Expert");

            difficultyLevelRepository.save(beginner);
            difficultyLevelRepository.save(intermediate);
            difficultyLevelRepository.save(expert);

            // Create hours per week

            // 1-50 hours per week
            for (int i = 1; i <= 50; i++) {
                HoursPerWeek hoursPerWeek = new HoursPerWeek();
                hoursPerWeek.setHours(i);
                hoursPerWeekRepository.save(hoursPerWeek);
            }

            HoursPerWeek hours20 = new HoursPerWeek(20, 20, null);
            HoursPerWeek hours40 = new HoursPerWeek(40, 40, null);
            HoursPerWeek hours10 = new HoursPerWeek(10, 10, null);
            HoursPerWeek hours5 = new HoursPerWeek(5, 5, null);
            HoursPerWeek hours4 = new HoursPerWeek(4, 4, null);

            // Create related certifications
            RelatedCertificate javaCertification = new RelatedCertificate();
            javaCertification.setCertificateName("Java SE 17 Programmer Professional");

            RelatedCertificate sqlCertification = new RelatedCertificate();
            sqlCertification.setCertificateName("SQL Fundamentals");

            RelatedCertificate dotNetCertification = new RelatedCertificate();
            dotNetCertification.setCertificateName(".Net Developer Fundamentals");

            RelatedCertificate azureFundamentalsCertification = new RelatedCertificate();
            azureFundamentalsCertification.setCertificateName("AZ-900 Azure Fundamentals");

            RelatedCertificate azureAdministrationCertification = new RelatedCertificate();
            azureAdministrationCertification.setCertificateName("AZ-104 Microsoft Certified Cloud Administrator");

            RelatedCertificate awsCloudPractitionerCertification = new RelatedCertificate();
            awsCloudPractitionerCertification.setCertificateName("CLF-C02 AWS Certified Cloud Practitioner");

            RelatedCertificate seoCertification = new RelatedCertificate();
            seoCertification.setCertificateName("SEO Wizard");

            RelatedCertificate socialMediaMarketingCertification = new RelatedCertificate();
            socialMediaMarketingCertification.setCertificateName("Certified Social Alchemist");

            RelatedCertificate businessStrategyCertification = new RelatedCertificate();
            businessStrategyCertification.setCertificateName("Certified Strategic Business Architect (CSBA)");

            RelatedCertificate machineLearningCertification = new RelatedCertificate();
            machineLearningCertification.setCertificateName("Machine Learning Fundamentals");

            RelatedCertificate imageRecognitionCertification = new RelatedCertificate();
            imageRecognitionCertification.setCertificateName("Machine Vision Associate");

            RelatedCertificate databricksCertification = new RelatedCertificate();
            databricksCertification.setCertificateName("Databricks Practitioner");

            relatedCertificateRepository.save(javaCertification);
            relatedCertificateRepository.save(sqlCertification);
            relatedCertificateRepository.save(dotNetCertification);
            relatedCertificateRepository.save(azureFundamentalsCertification);
            relatedCertificateRepository.save(azureAdministrationCertification);
            relatedCertificateRepository.save(awsCloudPractitionerCertification);
            relatedCertificateRepository.save(seoCertification);
            relatedCertificateRepository.save(socialMediaMarketingCertification);
            relatedCertificateRepository.save(businessStrategyCertification);
            relatedCertificateRepository.save(machineLearningCertification);
            relatedCertificateRepository.save(imageRecognitionCertification);
            relatedCertificateRepository.save(databricksCertification);

            // Create topics
            Topic javaTopic = new Topic();
            javaTopic.setTopic("Java");

            Topic realTimeProgrammingTopic = new Topic();
            realTimeProgrammingTopic.setTopic("real-time programming");

            Topic multiThreadingTopic = new Topic();
            multiThreadingTopic.setTopic("multi-threading");

            Topic programmingTopic = new Topic();
            programmingTopic.setTopic("programming");

            Topic sqlTopic = new Topic();
            sqlTopic.setTopic("SQL");

            Topic relationalDatabasesTopic = new Topic();
            relationalDatabasesTopic.setTopic("relational databases");

            Topic mySQLTopic = new Topic();
            mySQLTopic.setTopic("MySQL");

            Topic webTopic = new Topic();
            webTopic.setTopic("web");

            Topic dotNetTopic = new Topic();
            dotNetTopic.setTopic(".net");

            Topic cSharpTopic = new Topic();
            cSharpTopic.setTopic("C#");

            Topic azureTopic = new Topic();
            azureTopic.setTopic("Azure");

            Topic cloudServicesTopic = new Topic();
            cloudServicesTopic.setTopic("cloud services");

            Topic keywordResearchTopic = new Topic();
            keywordResearchTopic.setTopic("keyword research and analysis");

            Topic technicalSEOTopic = new Topic();
            technicalSEOTopic.setTopic("technical SEO optimization");

            Topic offPageSEOTopic = new Topic();
            offPageSEOTopic.setTopic("off-page SEO strategies");

            Topic advancedAnalyticsTopic = new Topic();
            advancedAnalyticsTopic.setTopic("advanced analytics and reporting");

            Topic strategicStorytellingTopic = new Topic();
            strategicStorytellingTopic.setTopic("strategic storytelling");

            Topic targetedEngagementTopic = new Topic();
            targetedEngagementTopic.setTopic("targeted engagement techniques");

            Topic dataDrivenOptimizationTopic = new Topic();
            dataDrivenOptimizationTopic.setTopic("data-driven optimization");

            Topic pythonTopic = new Topic();
            pythonTopic.setTopic("Python");

            Topic machineLearningTopic = new Topic();
            machineLearningTopic.setTopic("machine learning");

            Topic dataScienceTopic = new Topic();
            dataScienceTopic.setTopic("data science");

            Topic neuralNetworksTopic = new Topic();
            neuralNetworksTopic.setTopic("neural networks");

            Topic imageProcessingTopic = new Topic();
            imageProcessingTopic.setTopic("image processing");

            Topic databricksTopic = new Topic();
            databricksTopic.setTopic("Databricks");

            topicRepository.save(javaTopic);
            topicRepository.save(realTimeProgrammingTopic);
            topicRepository.save(multiThreadingTopic);
            topicRepository.save(programmingTopic);
            topicRepository.save(sqlTopic);
            topicRepository.save(relationalDatabasesTopic);
            topicRepository.save(mySQLTopic);
            topicRepository.save(webTopic);
            topicRepository.save(dotNetTopic);
            topicRepository.save(cSharpTopic);
            topicRepository.save(azureTopic);
            topicRepository.save(cloudServicesTopic);
            topicRepository.save(keywordResearchTopic);
            topicRepository.save(technicalSEOTopic);
            topicRepository.save(offPageSEOTopic);
            topicRepository.save(advancedAnalyticsTopic);
            topicRepository.save(strategicStorytellingTopic);
            topicRepository.save(targetedEngagementTopic);
            topicRepository.save(dataDrivenOptimizationTopic);
            topicRepository.save(pythonTopic);
            topicRepository.save(machineLearningTopic);
            topicRepository.save(dataScienceTopic);
            topicRepository.save(neuralNetworksTopic);
            topicRepository.save(imageProcessingTopic);
            topicRepository.save(databricksTopic);

            Course javaCourseBase = Course.builder()
                    .courseName("Real-Time Programming in Java")
                    .description("""
                            Embark on a transformative learning experience with our expert-level online course, "RealTime Programming in Java."
                            Designed for seasoned developers and Java enthusiasts seeking mastery in
                            real-time applications, this advanced course delves deep into the intricacies of leveraging Java for
                            mission-critical systems. Explore cutting-edge concepts such as multithreading, synchronization, and
                            low-latency programming, equipping you with the skills needed to build responsive and robust real-time
                            solutions. Led by industry experts with extensive hands-on experience, this course combines theoretical
                            insights with practical application, ensuring you not only grasp the theoretical underpinnings but also
                            gain the proficiency to implement real-time solutions confidently. Elevate your Java programming
                            expertise to new heights and stay ahead in the ever-evolving landscape of real-time systems with our
                            comprehensive and immersive course.
                            """)
                    .startDate(LocalDateTime.of(2025, 6, 3, 0, 0))
                    .endDate(LocalDateTime.of(2025, 6, 28, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(itCategory)
                    .credit(credit7_5)
                    .currency(nok)
                    .difficultyLevel(expert)
                    .hoursPerWeek(hours40)
                    .topics(new HashSet<>(Arrays.asList(javaTopic, realTimeProgrammingTopic, multiThreadingTopic, programmingTopic)))
                    .relatedCertificates(new HashSet<>(List.of(javaCertification)))
                    .build();

            Course javaCourse = javaCourseBase.toBuilder()
                    .price(new BigDecimal("29999"))
                    .provider(ntnuProvider)
                    .build();

            Course javaCourse1 = javaCourseBase.toBuilder()
                    .price(new BigDecimal("32000"))
                    .provider(oracleProvider)
                    .build();

            Course sqlCourseBase = Course.builder()
                    .courseName("Introduction to SQL Essentials")
                    .description("""
                Dive into the fundamentals of database management with our beginner-level online course,
                "Introduction to SQL Essentials." Geared towards those new to the world of databases and SQL, this
                course provides a comprehensive foundation for understanding and utilizing SQL for effective data
                management. While MySQL is touched upon to broaden your practical knowledge, the core focus is on
                SQL's universal principles applicable across various database systems. Led by seasoned instructors, the
                course covers database design, querying data, and basic data manipulation using SQL commands. With a
                hands-on approach, you'll engage in practical exercises to reinforce your learning, ensuring you gain the
                skills necessary to navigate and interact with databases confidently. Whether you're a budding
                developer, analyst, or anyone eager to harness the power of databases, this course offers an accessible
                entry point into the world of SQL, setting the stage for your future success in data-driven environments.
                """)
                    .startDate(LocalDateTime.of(2025, 6, 10, 0, 0))
                    .endDate(LocalDateTime.of(2025, 6, 28, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(itCategory)
                    .credit(credit2)
                    .currency(nok)
                    .difficultyLevel(beginner)
                    .hoursPerWeek(hours20)
                    .topics(new HashSet<>(Arrays.asList(sqlTopic, relationalDatabasesTopic, mySQLTopic)))
                    .relatedCertificates(new HashSet<>(List.of(sqlCertification)))
                    .build();

            Course sqlCourse = sqlCourseBase.toBuilder()
                    .price(new BigDecimal("8000"))
                    .provider(apacheProvider)
                    .build();

            Course sqlCourse1 = sqlCourseBase.toBuilder()
                    .price(new BigDecimal("10000"))
                    .provider(ntnuProvider)
                    .build();

            Course sqlCourse2 = sqlCourseBase.toBuilder()
                    .price(new BigDecimal("8990"))
                    .provider(pearsonProvider)
                    .build();


            Course dotNetCourseBase = Course.builder()
                    .courseName("Creating Web Application with .Net")
                    .description("""
                Embark on your journey into web development with our beginner-level online course,
                "Creating Web Applications with .NET." Tailored for those stepping into the dynamic world of web
                development, this course provides a solid foundation in utilizing the versatile .NET framework to build
                powerful and interactive web applications. Guided by experienced instructors, you'll explore
                fundamental concepts such as web application architecture, user interface design, and server-side
                scripting using .NET technologies like ASP.NET. Throughout the course, you'll engage in hands-on
                projects that walk you through the entire development process, from designing responsive user
                interfaces to implementing server-side functionality. Gain practical skills in C# programming and
                discover how to leverage the robust features of .NET to bring your web applications to life. Whether
                you're a programming novice or transitioning from another language, this course offers a welcoming
                entry point into the exciting realm of web application development with .NET, setting you on a path to
                create dynamic and engaging online experiences.
                """)
                    .startDate(LocalDateTime.of(2025, 8, 5, 0, 0))
                    .endDate(LocalDateTime.of(2025, 8, 16, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(itCategory)
                    .credit(credit4)
                    .currency(nok)
                    .difficultyLevel(beginner)
                    .hoursPerWeek(hours40)
                    .topics(new HashSet<>(Arrays.asList(webTopic, dotNetTopic, cSharpTopic)))
                    .relatedCertificates(new HashSet<>(List.of(dotNetCertification)))
                    .build();

            Course dotNetCourse = dotNetCourseBase.toBuilder()
                    .price(new BigDecimal("2999"))
                    .provider(microsoftProvider)
                    .build();

            Course dotNetCourse1 = dotNetCourseBase.toBuilder()
                    .price(new BigDecimal("3000"))
                    .provider(pearsonProvider)
                    .build();

            Course dotNetCourse2 = dotNetCourseBase.toBuilder()
                    .price(new BigDecimal("2000"))
                    .provider(oracleProvider)
                    .build();

            Course azureFundamentalsCourseBase = Course.builder()
                    .courseName("Azure Cloud Fundamentals")
                    .description("""
                Embark on your cloud computing journey with our beginner-level online course, "Azure
                Fundamentals," meticulously crafted to prepare you for the AZ-900 exam. Whether you're new to cloud
                technologies or seeking to validate your foundational knowledge, this course provides a comprehensive
                introduction to Microsoft Azure, one of the industry's leading cloud platforms. Delve into the essentials
                of cloud concepts, Azure services, pricing, and compliance, all while guided by expert instructors who
                understand the importance of building a strong cloud foundation. Through a combination of engaging
                lectures, hands-on labs, and real-world scenarios, you'll gain practical insights into deploying solutions
                on Azure and mastering fundamental cloud principles. By the end of the course, you'll not only be well
                prepared to ace the AZ-900 exam but will also have a solid understanding of Azure's capabilities,
                empowering you to confidently navigate the vast landscape of cloud computing. Join us on this
                educational journey and unlock the potential of cloud technology with Azure Fundamentals.
                """)
                    .startDate(LocalDateTime.of(2025, 8, 5, 0, 0))
                    .endDate(LocalDateTime.of(2025, 8, 30, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(itCategory)
                    .credit(credit2)
                    .currency(nok)
                    .difficultyLevel(beginner)
                    .hoursPerWeek(hours10)
                    .topics(new HashSet<>(Arrays.asList(azureTopic, cloudServicesTopic)))
                    .relatedCertificates(new HashSet<>(List.of(azureFundamentalsCertification)))
                    .build();

            Course azureFundamentalsCourse = azureFundamentalsCourseBase.toBuilder()
                    .price(new BigDecimal("2000"))
                    .provider(microsoftProvider)
                    .build();

            Course azureFundamentalsCourse1 = azureFundamentalsCourseBase.toBuilder()
                    .price(new BigDecimal("1800"))
                    .provider(ntnuProvider)
                    .build();

            Course azureFundamentalsCourse2 = azureFundamentalsCourseBase.toBuilder()
                    .price(new BigDecimal("2000"))
                    .provider(pearsonProvider)
                    .build();

            Course azureAdministrationCourseBase = Course.builder()
                    .courseName("Azure Administration")
                    .description("""
                Elevate your cloud expertise with our intermediate-level online course, "Azure
                Administrator," meticulously designed to prepare you for the AZ-104 exam – your gateway to becoming
                a Microsoft Certified Cloud Administrator. Tailored for individuals with a foundational understanding of
                Azure, this course takes a deep dive into advanced administration and management tasks, honing the
                skills required for efficient cloud operations. Led by seasoned Azure professionals, you'll explore
                intricate topics such as virtual networking, identity management, and governance strategies, gaining
                hands-on experience through practical exercises and real-world scenarios. The course's comprehensive
                coverage aligns seamlessly with the AZ-104 exam objectives, ensuring that you not only pass the
                certification but emerge as a proficient Azure Administrator capable of implementing robust cloud
                solutions. Whether you're looking to enhance your career or solidify your position as a cloud expert, this
                course is your key to mastering the intricacies of Azure administration and achieving Microsoft Certified
                Cloud Administrator status. Join us on this transformative journey towards advanced Azure proficiency.
                """)
                    .startDate(LocalDateTime.of(2025, 9, 2, 0, 0))
                    .endDate(LocalDateTime.of(2025, 12, 20, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(itCategory)
                    .credit(credit4)
                    .currency(nok)
                    .difficultyLevel(intermediate)
                    .hoursPerWeek(hours5)
                    .topics(new HashSet<>(Arrays.asList(azureTopic, cloudServicesTopic)))
                    .relatedCertificates(new HashSet<>(List.of(azureAdministrationCertification)))
                    .build();

            Course azureAdministrationCourse = azureAdministrationCourseBase.toBuilder()
                    .price(new BigDecimal("4000"))
                    .provider(microsoftProvider)
                    .build();

            Course azureAdministrationCourse1 = azureAdministrationCourseBase.toBuilder()
                    .price(new BigDecimal("3600"))
                    .provider(ntnuProvider)
                    .build();

            Course azureAdministrationCourse2 = azureAdministrationCourseBase.toBuilder()
                    .price(new BigDecimal("4000"))
                    .provider(pearsonProvider)
                    .build();

            Course awsCloudPractitionerCourseBase = Course.builder()
                    .courseName("AWS Cloud Practitioner")
                    .description("""
                Discover the fundamentals of cloud computing in our beginner-level online course, "AWS
                Cloud Practitioner," designed to prepare you for the CLF-C02 certification exam. Tailored for individuals
                with minimal prior experience in cloud technologies, this course provides a robust foundation in
                understanding the essential concepts of Amazon Web Services (AWS). Led by experienced AWS
                professionals, the course delves into core topics, including cloud architecture, AWS services, security,
                and pricing models. Through dynamic lectures and hands-on labs, you'll gain practical insights into
                navigating the AWS console, setting up basic infrastructure, and comprehending key cloud principles. By
                the course's end, you'll be well-equipped to excel in the CLF-C02 exam and possess a foundational
                understanding of AWS, empowering you to confidently explore and leverage cloud services. Join us in
                this educational journey, and initiate your AWS Cloud Practitioner certification with assurance and
                proficiency.
                """)
                    .startDate(LocalDateTime.of(2025, 9, 9, 0, 0))
                    .endDate(LocalDateTime.of(2025, 9, 20, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(itCategory)
                    .credit(credit2)
                    .currency(nok)
                    .difficultyLevel(beginner)
                    .hoursPerWeek(hours20)
                    .topics(new HashSet<>(List.of(cloudServicesTopic)))
                    .relatedCertificates(new HashSet<>(List.of(awsCloudPractitionerCertification)))
                    .build();

            Course awsCloudPractitionerCourse = awsCloudPractitionerCourseBase.toBuilder()
                    .price(new BigDecimal("1000"))
                    .provider(amazonProvider)
                    .build();

            Course awsCloudPractitionerCourse1 = awsCloudPractitionerCourseBase.toBuilder()
                    .price(new BigDecimal("1200"))
                    .provider(pearsonProvider)
                    .build();

            Course awsCloudPractitionerCourse2 = awsCloudPractitionerCourseBase.toBuilder()
                    .price(new BigDecimal("1800"))
                    .provider(ntnuProvider)
                    .build();

            Course seoCourseBase = Course.builder()
                    .courseName("Search Engine Optimization")
                    .description("""
                Deepen your expertise in the digital landscape with our intermediate-level online course,
                "Search Engine Optimization (SEO)." Tailored for marketers, business owners, and digital enthusiasts
                looking to refine their online presence, this course takes a comprehensive dive into the intricacies of
                SEO strategies and techniques. Led by seasoned SEO professionals, the course covers advanced topics
                such as keyword research, on-page and off-page optimization, technical SEO, and analytics. Through a
                blend of theoretical insights and practical exercises, you'll learn how to enhance website visibility,
                improve search engine rankings, and drive organic traffic effectively. Stay ahead in the ever-evolving
                digital landscape by mastering the art and science of SEO. Whether you're aiming to boost your
                business's online visibility or embark on a career in digital marketing, this course equips you with the
                skills and knowledge needed to navigate the complexities of SEO with confidence and success. Join us
                and elevate your digital presence with our intermediate-level SEO course.
                """)
                    .startDate(LocalDateTime.of(2025, 8, 5, 0, 0))
                    .endDate(LocalDateTime.of(2025, 8, 30, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(dmCategory)
                    .credit(credit2)
                    .currency(nok)
                    .difficultyLevel(intermediate)
                    .hoursPerWeek(hours4)
                    .topics(new HashSet<>(Arrays.asList(keywordResearchTopic, technicalSEOTopic, offPageSEOTopic, advancedAnalyticsTopic)))
                    .relatedCertificates(new HashSet<>(List.of(seoCertification)))
                    .build();

            Course seoCourse = seoCourseBase.toBuilder()
                    .price(new BigDecimal("66000"))
                    .provider(adobeProvider)
                    .build();

            Course seoCourse1 = seoCourseBase.toBuilder()
                    .price(new BigDecimal("80000"))
                    .provider(appleProvider)
                    .build();

            Course seoCourse2 = seoCourseBase.toBuilder()
                    .price(new BigDecimal("60000"))
                    .provider(googleProvider)
                    .build();

            Course seoCourse3 = seoCourseBase.toBuilder()
                    .price(new BigDecimal("60000"))
                    .provider(microsoftProvider)
                    .build();

            Course seoCourse4 = seoCourseBase.toBuilder()
                    .price(new BigDecimal("60000"))
                    .provider(amazonProvider)
                    .build();

            Course socialMediaMarketingCourseBase = Course.builder()
                    .courseName("Social Media Marketing")
                    .description("""
                Elevate your digital marketing prowess with our intermediate-level online course, "Social
                Media Marketing." Tailored for marketers, business professionals, and enthusiasts seeking to harness
                the power of social platforms, this course provides an in-depth exploration of advanced social media
                marketing strategies. Led by industry experts, you'll delve into nuanced topics such as audience
                targeting, content optimization, social media advertising, and analytics. Through a blend of theoretical
                insights and hands-on exercises, you'll gain practical skills to create compelling social media campaigns,
                foster audience engagement, and measure the impact of your efforts. Stay at the forefront of digital
                marketing trends by mastering the art of crafting impactful narratives, building brand loyalty, and
                leveraging diverse social channels. Whether you aim to enhance your business's online presence or
                advance your career in digital marketing, this course equips you with the tools and knowledge to
                navigate the dynamic landscape of social media marketing with confidence and proficiency. Join us and
                amplify your social media marketing expertise with our intermediate-level course.
                """)
                    .startDate(LocalDateTime.of(2025, 8, 5, 0, 0))
                    .endDate(LocalDateTime.of(2025, 8, 30, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(dmCategory)
                    .credit(credit2)
                    .currency(nok)
                    .difficultyLevel(intermediate)
                    .hoursPerWeek(hours4)
                    .topics(new HashSet<>(Arrays.asList(strategicStorytellingTopic, targetedEngagementTopic, dataDrivenOptimizationTopic)))
                    .relatedCertificates(new HashSet<>(List.of(socialMediaMarketingCertification)))
                    .build();

            Course socialMediaMarketingCourse = socialMediaMarketingCourseBase.toBuilder()
                    .price(new BigDecimal("66000"))
                    .provider(adobeProvider)
                    .build();

            Course socialMediaMarketingCourse1 = socialMediaMarketingCourseBase.toBuilder()
                    .price(new BigDecimal("80000"))
                    .provider(appleProvider)
                    .build();

            Course socialMediaMarketingCourse2 = socialMediaMarketingCourseBase.toBuilder()
                    .price(new BigDecimal("60000"))
                    .provider(googleProvider)
                    .build();

            Course socialMediaMarketingCourse3 = socialMediaMarketingCourseBase.toBuilder()
                    .price(new BigDecimal("60000"))
                    .provider(microsoftProvider)
                    .build();

            Course socialMediaMarketingCourse4 = socialMediaMarketingCourseBase.toBuilder()
                    .price(new BigDecimal("60000"))
                    .provider(amazonProvider)
                    .build();

            Course businessStrategyCourseBase = Course.builder()
                    .courseName("Business Strategy")
                    .description("""
                Master the art of strategic thinking with our expert-level online course, "Business Strategy."
                Tailored for seasoned professionals, entrepreneurs, and strategic leaders, this course offers an
                immersive exploration of advanced business strategy concepts and applications. Led by industry thought
                leaders, you'll delve into intricate topics such as competitive analysis, market positioning, disruptive
                innovation, and global strategic management. Through case studies, simulations, and real-world
                scenarios, you'll sharpen your ability to make informed strategic decisions that drive long-term success.
                This course goes beyond the basics, challenging you to synthesize diverse business elements into a
                cohesive and forward-thinking strategy. Whether you aspire to lead a multinational corporation or
                refine your entrepreneurial ventures, our expert-level Business Strategy course empowers you to
                navigate complex business landscapes with foresight and precision. Join us in this transformative
                learning journey and elevate your strategic acumen to new heights.
                """)
                    .startDate(LocalDateTime.of(2025, 6, 3, 0, 0))
                    .endDate(LocalDateTime.of(2025, 11, 29, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(beCategory)
                    .credit(credit10)
                    .currency(nok)
                    .difficultyLevel(expert)
                    .hoursPerWeek(hours10)
                    .relatedCertificates(new HashSet<>(List.of(businessStrategyCertification)))
                    .build();

            Course businessStrategyCourse = businessStrategyCourseBase.toBuilder()
                    .price(new BigDecimal("50000"))
                    .provider(ntnuProvider)
                    .build();

            Course businessStrategyCourse1 = businessStrategyCourseBase.toBuilder()
                    .price(new BigDecimal("50000"))
                    .provider(microsoftProvider)
                    .build();

            Course businessStrategyCourse2 = businessStrategyCourseBase.toBuilder()
                    .price(new BigDecimal("50000"))
                    .provider(biProvider)
                    .build();

            Course machineLearningCourseBase = Course.builder()
                    .courseName("Machine Learning Basics with Python")
                    .description("""
                Embark on your journey into the exciting realm of artificial intelligence with our beginner
                level online course, "Machine Learning Basics with Python." Tailored for individuals new to the world of
                machine learning, this course provides a comprehensive introduction to the fundamental concepts and
                techniques using the versatile Python programming language. Led by experienced instructors, you'll
                explore the basics of supervised and unsupervised learning, delve into popular machine learning
                algorithms, and gain hands-on experience through practical exercises. No prior coding experience is
                required, making this course an ideal starting point for beginners eager to grasp the essentials of
                machine learning. By the end of the course, you'll have a solid foundation in using Python for machine
                learning applications, empowering you to unravel the mysteries of data and embark on a fascinating
                journey into the world of intelligent algorithms. Join us and demystify the basics of machine learning
                with Python in this accessible and empowering course.
                """)
                    .startDate(LocalDateTime.of(2025, 8, 19, 0, 0))
                    .endDate(LocalDateTime.of(2025, 8, 30, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(dsaCategory)
                    .credit(credit2)
                    .currency(nok)
                    .difficultyLevel(beginner)
                    .hoursPerWeek(hours10)
                    .topics(new HashSet<>(Arrays.asList(pythonTopic, machineLearningTopic, programmingTopic, dataScienceTopic)))
                    .relatedCertificates(new HashSet<>(List.of(machineLearningCertification)))
                    .build();

            Course machineLearningCourse = machineLearningCourseBase.toBuilder()
                    .price(new BigDecimal("20000"))
                    .provider(ntnuProvider)
                    .build();

            Course machineLearningCourse1 = machineLearningCourseBase.toBuilder()
                    .price(new BigDecimal("20000"))
                    .provider(uioProvider)
                    .build();

            Course machineLearningCourse2 = machineLearningCourseBase.toBuilder()
                    .price(new BigDecimal("20000"))
                    .provider(uibProvider)
                    .build();

            Course imageRecognitionCourseBase = Course.builder()
                    .courseName("Image Recognition")
                    .description("""
                Deepen your expertise in the realm of artificial intelligence with our intermediate-level
                online course, "Image Recognition with Python." Tailored for those with a foundational understanding of
                machine learning, this course immerses you in the intricacies of image recognition techniques and
                technologies using the powerful Python programming language. Led by seasoned instructors, you'll
                explore advanced concepts such as convolutional neural networks (CNNs), image preprocessing, and
                transfer learning. Through hands-on projects and real-world applications, you'll sharpen your skills in
                training models to recognize and classify images with precision. This course is ideal for individuals
                looking to expand their knowledge in computer vision and image processing, and it serves as a stepping
                stone for professionals aspiring to integrate image recognition capabilities into their projects. Join us in
                this intermediate-level course, and unlock the potential of image recognition with Python, advancing
                your proficiency in the exciting field of artificial intelligence.
                """)
                    .startDate(LocalDateTime.of(2025, 9, 2, 0, 0))
                    .endDate(LocalDateTime.of(2025, 9, 27, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(dsaCategory)
                    .credit(credit4)
                    .currency(nok)
                    .difficultyLevel(intermediate)
                    .hoursPerWeek(hours20)
                    .topics(new HashSet<>(Arrays.asList(pythonTopic, machineLearningTopic, programmingTopic, dataScienceTopic, neuralNetworksTopic, imageProcessingTopic)))
                    .relatedCertificates(new HashSet<>(List.of(imageRecognitionCertification)))
                    .build();

            Course imageRecognitionCourse = imageRecognitionCourseBase.toBuilder()
                    .price(new BigDecimal("30000"))
                    .provider(ntnuProvider)
                    .build();

            Course imageRecognitionCourse1 = imageRecognitionCourseBase.toBuilder()
                    .price(new BigDecimal("30000"))
                    .provider(uioProvider)
                    .build();

            Course imageRecognitionCourse2 = imageRecognitionCourseBase.toBuilder()
                    .price(new BigDecimal("30000"))
                    .provider(uibProvider)
                    .build();

            Course databricksCourseBase = Course.builder()
                    .courseName("Databricks Fundamentals")
                    .description("""
                Embark on your data journey with our beginner-level online course, "Databricks
                Fundamentals." Designed for individuals new to the world of big data and analytics, this course offers a
                comprehensive introduction to the essential concepts of Databricks, a leading unified analytics platform.
                Led by experienced instructors, you'll navigate through the fundamentals of data exploration, data
                engineering, and collaborative data science using Databricks. No prior experience with big data
                technologies is required, making this course an ideal starting point for beginners eager to harness the
                power of Databricks for streamlined data processing and analysis. By the end of the course, you'll have a
                solid foundation in using Databricks to uncover insights from large datasets, setting you on a path
                towards mastering the intricacies of modern data analytics. Join us and demystify the fundamentals of
                Databricks in this accessible and empowering course.
                """)
                    .startDate(LocalDateTime.of(2025, 8, 19, 0, 0))
                    .endDate(LocalDateTime.of(2025, 8, 30, 0, 0))
                    .active(true)
                    .created(LocalDateTime.now())
                    .category(dsaCategory)
                    .credit(credit2)
                    .currency(nok)
                    .difficultyLevel(beginner)
                    .hoursPerWeek(hours10)
                    .topics(new HashSet<>(Arrays.asList(pythonTopic, machineLearningTopic, programmingTopic, dataScienceTopic, neuralNetworksTopic, databricksTopic)))
                    .relatedCertificates(new HashSet<>(List.of(databricksCertification)))
                    .build();

            Course databricksCourse = databricksCourseBase.toBuilder()
                    .price(new BigDecimal("20000"))
                    .provider(ntnuProvider)
                    .build();

            Course databricksCourse1 = databricksCourseBase.toBuilder()
                    .price(new BigDecimal("20000"))
                    .provider(uioProvider)
                    .build();

            Course databricksCourse2 = databricksCourseBase.toBuilder()
                    .price(new BigDecimal("20000"))
                    .provider(uibProvider)
                    .build();

            // All courses
            List<Course> courses = Arrays.asList(
                    javaCourse, javaCourse1,
                    sqlCourse, sqlCourse1, sqlCourse2,
                    dotNetCourse, dotNetCourse1, dotNetCourse2,
                    azureFundamentalsCourse, azureFundamentalsCourse1, azureFundamentalsCourse2,
                    azureAdministrationCourse, azureAdministrationCourse1, azureAdministrationCourse2,
                    awsCloudPractitionerCourse, awsCloudPractitionerCourse1, awsCloudPractitionerCourse2,
                    seoCourse, seoCourse1, seoCourse2, seoCourse3, seoCourse4,
                    socialMediaMarketingCourse, socialMediaMarketingCourse1, socialMediaMarketingCourse2, socialMediaMarketingCourse3, socialMediaMarketingCourse4,
                    businessStrategyCourse, businessStrategyCourse1, businessStrategyCourse2,
                    machineLearningCourse, machineLearningCourse1, machineLearningCourse2,
                    imageRecognitionCourse, imageRecognitionCourse1, imageRecognitionCourse2,
                    databricksCourse, databricksCourse1, databricksCourse2
            );

            courseRepository.saveAll(courses);

            // Reviews
            courses.forEach(course -> {
                for (int i = 0; i < 10; i++) {
                    Review review = Review.builder()
                            .rating(RANDOM.nextInt(5) + 1)
                            .review("This is a dummy review for " + course.getCourseName())
                            .created(LocalDateTime.now().minusDays(RANDOM.nextInt(365)))
                            .helpfulVotes(RANDOM.nextInt(100))
                            .reported(RANDOM.nextBoolean())
                            .user(adminUser2)
                            .course(course)
                            .build();

                    reviewRepository.save(review);
                }
            });


            // Order Status
            this.orderStatusRepository.saveAll(
                    Arrays.stream(OrderStatusEnum.values())
                            .map(statusEnum -> {
                                OrderStatus orderStatus = new OrderStatus();
                                orderStatus.setStatus(statusEnum);
                                return orderStatus;
                            })
                            .toList()
            );

            // Payment Methods
            this.paymentMethodRepository.saveAll(
                    Arrays.stream(PaymentMethodEnum.values())
                            .map(methodEnum -> {
                                PaymentMethod paymentMethod = new PaymentMethod();
                                paymentMethod.setMethod(methodEnum);
                                return paymentMethod;
                            })
                            .toList()
            );

        }

        log.info("Test data imported successfully.");
    }
}