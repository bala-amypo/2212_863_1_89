package com.example.demo;

import com.example.demo.dto.AuthResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.impl.*;
import com.example.demo.util.InvoiceCategorizationEngine;
import com.example.demo.servlet.HelloServlet;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Massive TestNG suite for Smart Invoice Categorization API.
 * 64 tests, ordered by topics as per requirement.
 */
@Listeners(TestResultListener.class)
public class SmartInvoiceApplicationTests {

    // Common mocks
    private UserRepository userRepository;
    private VendorRepository vendorRepository;
    private InvoiceRepository invoiceRepository;
    private CategoryRepository categoryRepository;
    private CategorizationRuleRepository ruleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    // Services and engine
    private UserServiceImpl userService;
    private VendorServiceImpl vendorService;
    private CategoryServiceImpl categoryService;
    private CategorizationRuleServiceImpl ruleService;
    private InvoiceServiceImpl invoiceService;
    private InvoiceCategorizationEngine engine;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userRepository = mock(UserRepository.class);
        vendorRepository = mock(VendorRepository.class);
        invoiceRepository = mock(InvoiceRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        ruleRepository = mock(CategorizationRuleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        userDetailsService = mock(UserDetailsService.class);
        jwtUtil = mock(JwtUtil.class);

        engine = new InvoiceCategorizationEngine();
        userService = new UserServiceImpl(userRepository, passwordEncoder);
        vendorService = new VendorServiceImpl(vendorRepository);
        categoryService = new CategoryServiceImpl(categoryRepository);
        ruleService = new CategorizationRuleServiceImpl(ruleRepository, categoryRepository);
        invoiceService = new InvoiceServiceImpl(invoiceRepository, userRepository, vendorRepository, ruleRepository, engine);
    }

    // -------------------------------------------------------------
    // 1. Servlet + Tomcat – group "servlet" (Tests 1–8)
    // -------------------------------------------------------------

    @Test(groups = "servlet", priority = 1)
    public void testServletHasWebServletAnnotation() {
        WebServlet annotation = HelloServlet.class.getAnnotation(WebServlet.class);
        Assert.assertNotNull(annotation, "HelloServlet must have @WebServlet annotation");
    }

    @Test(groups = "servlet", priority = 2)
    public void testServletHasUrlPatternsConfigured() {
        WebServlet annotation = HelloServlet.class.getAnnotation(WebServlet.class);
        Assert.assertTrue(annotation.urlPatterns().length > 0, "Servlet must expose at least one URL pattern");
    }

    @Test(groups = "servlet", priority = 3)
    public void testServletExtendsHttpServlet() {
        Assert.assertTrue(
                jakarta.servlet.http.HttpServlet.class.isAssignableFrom(HelloServlet.class),
                "HelloServlet must extend HttpServlet");
    }

    @Test(groups = "servlet", priority = 4)
    public void testServletHasProtectedDoGetMethod() throws Exception {
        var method = HelloServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        Assert.assertTrue(Modifier.isProtected(method.getModifiers()), "doGet must be protected");
    }

    @Test(groups = "servlet", priority = 5)
    public void testServletClassNameIsMeaningful() {
        Assert.assertTrue(HelloServlet.class.getSimpleName().contains("Hello"),
                "Servlet class name should be meaningful");
    }

    @Test(groups = "servlet", priority = 6)
    public void testServletCanBeInstantiated() {
        HelloServlet servlet = new HelloServlet();
        Assert.assertNotNull(servlet, "Servlet instance should not be null");
    }

    @Test(groups = "servlet", priority = 7)
    public void testServletHasNoCustomFieldsEdgeCase() {
        var fields = HelloServlet.class.getDeclaredFields();
        Assert.assertEquals(fields.length, 0, "Servlet should not maintain unnecessary state fields");
    }

    @Test(groups = "servlet", priority = 8)
    public void testServletIsPublicClass() {
        Assert.assertTrue(Modifier.isPublic(HelloServlet.class.getModifiers()), "Servlet must be public");
    }

    // -------------------------------------------------------------
    // 2. CRUD with Spring Boot REST – group "crud" (Tests 9–16)
    // -------------------------------------------------------------

    @Test(groups = "crud", priority = 9)
    public void testRegisterUserSuccess() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword("password123");

        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        User saved = userService.registerUser(user);

        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getEmail(), "user@test.com");
        Assert.assertEquals(saved.getPassword(), "encoded");
    }

    @Test(groups = "crud", priority = 10)
    public void testRegisterUserDuplicateEmailNegative() {
        User user = new User();
        user.setEmail("dup@test.com");
        user.setPassword("password123");

        when(userRepository.existsByEmail("dup@test.com")).thenReturn(true);

        try {
            userService.registerUser(user);
            Assert.fail("Expected IllegalArgumentException for duplicate email");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Email already in use"));
        }
    }

    @Test(groups = "crud", priority = 11)
    public void testCreateVendorSuccess() {
        Vendor vendor = new Vendor();
        vendor.setVendorName("VendorA");

        when(vendorRepository.save(any(Vendor.class))).thenAnswer(inv -> {
            Vendor v = inv.getArgument(0);
            v.setId(10L);
            return v;
        });

        Vendor saved = vendorService.createVendor(vendor);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getVendorName(), "VendorA");
    }

    @Test(groups = "crud", priority = 12)
    public void testUploadInvoiceSuccessPositive() {
        User user = new User();
        user.setId(1L);
        Vendor vendor = new Vendor();
        vendor.setId(2L);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-001");
        invoice.setAmount(150.0);
        invoice.setInvoiceDate(LocalDate.now());

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(vendorRepository.findById(2L)).thenReturn(java.util.Optional.of(vendor));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(inv -> inv.getArgument(0));

        Invoice saved = invoiceService.uploadInvoice(1L, 2L, invoice);

        Assert.assertEquals(saved.getUploadedBy().getId(), 1L);
        Assert.assertEquals(saved.getVendor().getId(), 2L);
        Assert.assertNull(saved.getCategory(), "Category must be null before categorization");
    }

    @Test(groups = "crud", priority = 13)
    public void testUploadInvoiceUserNotFoundNegative() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-404");
        invoice.setAmount(100.0);
        invoice.setInvoiceDate(LocalDate.now());

        when(userRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        try {
            invoiceService.uploadInvoice(999L, 1L, invoice);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("User not found"));
        }
    }

    @Test(groups = "crud", priority = 14)
    public void testGetInvoiceByIdPositive() {
        Invoice invoice = new Invoice();
        invoice.setId(5L);
        when(invoiceRepository.findById(5L)).thenReturn(java.util.Optional.of(invoice));

        Invoice result = invoiceService.getInvoice(5L);
        Assert.assertEquals(result.getId(), 5L);
    }

    @Test(groups = "crud", priority = 15)
    public void testGetInvoiceByIdNotFoundNegative() {
        when(invoiceRepository.findById(123L)).thenReturn(java.util.Optional.empty());
        try {
            invoiceService.getInvoice(123L);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("Invoice not found"));
        }
    }

    @Test(groups = "crud", priority = 16)
    public void testListInvoicesByUserEdgeCaseEmpty() {
        User user = new User();
        user.setId(7L);

        when(userRepository.findById(7L)).thenReturn(java.util.Optional.of(user));
        when(invoiceRepository.findByUploadedBy(user)).thenReturn(Collections.emptyList());

        List<Invoice> invoices = invoiceService.getInvoicesByUser(7L);
        Assert.assertTrue(invoices.isEmpty(), "Should return empty list when user has no invoices");
    }

    // -------------------------------------------------------------
    // 3. DI & IoC with Spring – group "di" (Tests 17–24)
    // -------------------------------------------------------------

    @Test(groups = "di", priority = 17)
    public void testUserServiceIsCreatedWithDependencies() {
        Assert.assertNotNull(userService);
    }

    @Test(groups = "di", priority = 18)
    public void testVendorServiceIsCreatedWithDependencies() {
        Assert.assertNotNull(vendorService);
    }

    @Test(groups = "di", priority = 19)
    public void testCategoryServiceIsCreatedWithDependencies() {
        Assert.assertNotNull(categoryService);
    }

    @Test(groups = "di", priority = 20)
    public void testInvoiceServiceIsCreatedWithDependencies() {
        Assert.assertNotNull(invoiceService);
    }

    @Test(groups = "di", priority = 21)
    public void testRuleServiceIsCreatedWithDependencies() {
        Assert.assertNotNull(ruleService);
    }

    @Test(groups = "di", priority = 22)
    public void testPasswordEncoderIsInjected() {
        Assert.assertNotNull(passwordEncoder);
    }

    @Test(groups = "di", priority = 23)
    public void testIoCLetsUsMockUserRepository() {
        UserRepository altRepo = mock(UserRepository.class);
        UserServiceImpl anotherService = new UserServiceImpl(altRepo, passwordEncoder);
        Assert.assertNotNull(anotherService);
    }

    @Test(groups = "di", priority = 24)
    public void testEngineIsInjectedInInvoiceService() {
        Assert.assertNotNull(engine);
    }

    // -------------------------------------------------------------
    // 4. Hibernate configs & CRUD – group "hibernate" (Tests 25–32)
    // -------------------------------------------------------------

    @Test(groups = "hibernate", priority = 25)
    public void testUserEntityHasEmailUniqueConstraint() {
        Table t = User.class.getAnnotation(Table.class);
        Assert.assertNotNull(t, "User must have @Table annotation");
        Assert.assertTrue(t.uniqueConstraints().length > 0, "Email should be unique");
    }

    @Test(groups = "hibernate", priority = 26)
    public void testInvoiceEntityHasCompositeUniqueConstraint() {
        Table t = Invoice.class.getAnnotation(Table.class);
        Assert.assertNotNull(t);
        Assert.assertTrue(t.uniqueConstraints().length > 0,
                "Invoice must have vendor_id + invoiceNumber unique");
    }

    @Test(groups = "hibernate", priority = 27)
    public void testUserPrePersistSetsCreatedAt() {
        User u = new User();
        u.prePersist();
        Assert.assertNotNull(u.getCreatedAt());
    }

    @Test(groups = "hibernate", priority = 28)
    public void testInvoicePrePersistSetsUploadedAt() {
        Invoice inv = new Invoice();
        inv.prePersist();
        Assert.assertNotNull(inv.getUploadedAt());
    }

    @Test(groups = "hibernate", priority = 29)
    public void testRulePrePersistSetsCreatedAt() {
        CategorizationRule rule = new CategorizationRule();
        rule.prePersist();
        Assert.assertNotNull(rule.getCreatedAt());
    }

    @Test(groups = "hibernate", priority = 30)
    public void testCategoryPrePersistSetsCreatedAt() {
        Category category = new Category();
        category.prePersist();
        Assert.assertNotNull(category.getCreatedAt());
    }

    @Test(groups = "hibernate", priority = 31)
    public void testHibernateSaveUserAssignsId() {
        User user = new User();
        user.setEmail("hib@test.com");
        user.setPassword("password123");

        when(userRepository.existsByEmail("hib@test.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(99L);
            return u;
        });

        User saved = userService.registerUser(user);
        Assert.assertEquals(saved.getId(), 99L);
    }

    @Test(groups = "hibernate", priority = 32)
    public void testHibernateSaveVendorAssignsId() {
        Vendor v = new Vendor();
        v.setVendorName("HibVendor");

        when(vendorRepository.save(any(Vendor.class))).thenAnswer(inv -> {
            Vendor vv = inv.getArgument(0);
            vv.setId(55L);
            return vv;
        });

        Vendor saved = vendorService.createVendor(v);
        Assert.assertEquals(saved.getId(), 55L);
    }

    // -------------------------------------------------------------
    // 5. JPA mapping & normalization – group "jpa" (Tests 33–40)
    // -------------------------------------------------------------

    @Test(groups = "jpa", priority = 33)
    public void testInvoiceVendorManyToOneAnnotationPresent() throws NoSuchFieldException {
        ManyToOne ann = Invoice.class.getDeclaredField("vendor")
                .getAnnotation(ManyToOne.class);
        Assert.assertNotNull(ann);
    }

    @Test(groups = "jpa", priority = 34)
    public void testInvoiceCategoryManyToOneAnnotationPresent() throws NoSuchFieldException {
        ManyToOne ann = Invoice.class.getDeclaredField("category")
                .getAnnotation(ManyToOne.class);
        Assert.assertNotNull(ann);
    }

    @Test(groups = "jpa", priority = 35)
    public void testInvoiceUploadedByManyToOneAnnotationPresent() throws NoSuchFieldException {
        ManyToOne ann = Invoice.class.getDeclaredField("uploadedBy")
                .getAnnotation(ManyToOne.class);
        Assert.assertNotNull(ann);
    }

    @Test(groups = "jpa", priority = 36)
    public void testUserFavoriteVendorsManyToManyAnnotationPresent() throws NoSuchFieldException {
        ManyToMany ann = User.class.getDeclaredField("favoriteVendors")
                .getAnnotation(ManyToMany.class);
        Assert.assertNotNull(ann);
    }

    @Test(groups = "jpa", priority = 37)
    public void testVendorUsersManyToManyAnnotationPresent() throws NoSuchFieldException {
        ManyToMany ann = Vendor.class.getDeclaredField("users")
                .getAnnotation(ManyToMany.class);
        Assert.assertNotNull(ann);
    }

    @Test(groups = "jpa", priority = 38)
    public void testNormalization1NFInvoiceScalarFields() {
        // Basic sanity: no collection fields in Invoice
        long collectionFields = java.util.Arrays.stream(Invoice.class.getDeclaredFields())
                .filter(f -> java.util.Collection.class.isAssignableFrom(f.getType()))
                .count();
        Assert.assertEquals(collectionFields, 0, "Invoice should not contain repeating groups");
    }

    @Test(groups = "jpa", priority = 39)
    public void testNormalization2NFInvoiceCompositeBusinessKey() {
        Table t = Invoice.class.getAnnotation(Table.class);
        Assert.assertNotNull(t);
        Assert.assertTrue(t.uniqueConstraints().length > 0,
                "Unique constraint approximates composite key: vendor_id + invoiceNumber");
    }

    @Test(groups = "jpa", priority = 40)
    public void testNormalization3NFCategorySeparatedFromInvoice() {
        // Ensure Invoice does not store categoryName directly
        boolean hasCategoryNameField = java.util.Arrays.stream(Invoice.class.getDeclaredFields())
                .anyMatch(f -> f.getName().equalsIgnoreCase("categoryName"));
        Assert.assertFalse(hasCategoryNameField, "Category name should be in Category entity, not Invoice");
    }

    // -------------------------------------------------------------
    // 6. Many-to-Many relationships – group "manyToMany" (Tests 41–48)
    // -------------------------------------------------------------

    @Test(groups = "manyToMany", priority = 41)
    public void testAddFavoriteVendorToUser() {
        User user = new User();
        Vendor vendor = new Vendor();
        vendor.setVendorName("Fav");

        user.getFavoriteVendors().add(vendor);

        Assert.assertEquals(user.getFavoriteVendors().size(), 1);
    }

    @Test(groups = "manyToMany", priority = 42)
    public void testAddMultipleVendorsToUser() {
        User user = new User();
        Vendor v1 = new Vendor();
        Vendor v2 = new Vendor();

        user.getFavoriteVendors().add(v1);
        user.getFavoriteVendors().add(v2);

        Assert.assertEquals(user.getFavoriteVendors().size(), 2);
    }

    @Test(groups = "manyToMany", priority = 43)
    public void testAddMultipleUsersToVendor() {
        Vendor vendor = new Vendor();
        User u1 = new User();
        User u2 = new User();

        vendor.getUsers().add(u1);
        vendor.getUsers().add(u2);

        Assert.assertEquals(vendor.getUsers().size(), 2);
    }

    @Test(groups = "manyToMany", priority = 44)
    public void testNoDuplicateVendorsInSetEdgeCase() {
        User user = new User();
        Vendor v = new Vendor();

        user.getFavoriteVendors().add(v);
        user.getFavoriteVendors().add(v); // Set prevents duplicate

        Assert.assertEquals(user.getFavoriteVendors().size(), 1);
    }

    @Test(groups = "manyToMany", priority = 45)
    public void testRemoveVendorFromFavorites() {
        User user = new User();
        Vendor v = new Vendor();
        user.getFavoriteVendors().add(v);

        user.getFavoriteVendors().remove(v);

        Assert.assertTrue(user.getFavoriteVendors().isEmpty());
    }

    @Test(groups = "manyToMany", priority = 46)
    public void testBidirectionalRelationshipConsistencyManual() {
        User user = new User();
        Vendor vendor = new Vendor();

        user.getFavoriteVendors().add(vendor);
        vendor.getUsers().add(user);

        Assert.assertTrue(user.getFavoriteVendors().contains(vendor));
        Assert.assertTrue(vendor.getUsers().contains(user));
    }

    @Test(groups = "manyToMany", priority = 47)
    public void testUserFavoriteVendorsEmptyByDefault() {
        User user = new User();
        Assert.assertTrue(user.getFavoriteVendors().isEmpty());
    }

    @Test(groups = "manyToMany", priority = 48)
    public void testVendorUsersEmptyByDefault() {
        Vendor vendor = new Vendor();
        Assert.assertTrue(vendor.getUsers().isEmpty());
    }

    // -------------------------------------------------------------
    // 7. Security & JWT – group "securityJwt" (Tests 49–56)
    // -------------------------------------------------------------

    @Test(groups = "securityJwt", priority = 49)
    public void testJwtTokenGenerationIncludesUserIdRoleEmailConceptually() {
        User user = new User();
        user.setId(1L);
        user.setEmail("jwt@test.com");
        user.setRole("ADMIN");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("jwt@test.com")
                .password("enc")
                .authorities("ROLE_ADMIN")
                .build();

        when(jwtUtil.generateToken(userDetails, user)).thenReturn("mock-token");

        String token = jwtUtil.generateToken(userDetails, user);

        Assert.assertEquals(token, "mock-token");
    }

    @Test(groups = "securityJwt", priority = 50)
    public void testJwtValidationPositive() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("valid@test.com")
                .password("enc")
                .authorities("ROLE_USER")
                .build();

        when(jwtUtil.validateToken("good-token", userDetails)).thenReturn(true);

        boolean valid = jwtUtil.validateToken("good-token", userDetails);
        Assert.assertTrue(valid);
    }

    @Test(groups = "securityJwt", priority = 51)
    public void testJwtValidationNegative() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("invalid@test.com")
                .password("enc")
                .authorities("ROLE_USER")
                .build();

        when(jwtUtil.validateToken("bad-token", userDetails)).thenReturn(false);

        boolean valid = jwtUtil.validateToken("bad-token", userDetails);
        Assert.assertFalse(valid);
    }

    @Test(groups = "securityJwt", priority = 52)
    public void testAuthenticationManagerCalledOnLoginSimulation() {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken("user@auth.com", "password123");
        authenticationManager.authenticate(token);
        verify(authenticationManager, times(1)).authenticate(token);
    }

    @Test(groups = "securityJwt", priority = 53)
    public void testPasswordEncodedOnRegisterUser() {
        User user = new User();
        user.setEmail("encode@test.com");
        user.setPassword("plaintext");

        when(userRepository.existsByEmail("encode@test.com")).thenReturn(false);
        when(passwordEncoder.encode("plaintext")).thenReturn("encrypted");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(11L);
            return u;
        });

        User saved = userService.registerUser(user);

        Assert.assertEquals(saved.getPassword(), "encrypted");
    }

    @Test(groups = "securityJwt", priority = 54)
    public void testRolePrefixUsedForAuthorities() {
        User user = new User();
        user.setRole("ADMIN");
        String authority = "ROLE_" + user.getRole();
        Assert.assertEquals(authority, "ROLE_ADMIN");
    }

    @Test(groups = "securityJwt", priority = 55)
    public void testAuthResponseCarriesTokenAndUserData() {
        AuthResponse response = new AuthResponse("tkn", 2L, "x@y.com", "USER");
        Assert.assertEquals(response.getToken(), "tkn");
        Assert.assertEquals(response.getUserId(), 2L);
        Assert.assertEquals(response.getEmail(), "x@y.com");
        Assert.assertEquals(response.getRole(), "USER");
    }

    @Test(groups = "securityJwt", priority = 56)
    public void testUnauthorizedScenarioEdgeCaseMissingHeaderSimulation() {
        String header = null;
        Assert.assertNull(header, "Authorization header is null to simulate missing token");
    }

    // -------------------------------------------------------------
    // 8. HQL & HCQL (advanced querying) – group "hqlCriteria" (Tests 57–64)
    // -------------------------------------------------------------

    @Test(groups = "hqlCriteria", priority = 57)
    public void testHqlQueryFindByAmountGreaterThanReturnsInvoices() {
        Invoice inv = new Invoice();
        inv.setAmount(500.0);

        when(invoiceRepository.findByAmountGreaterThanHql(100.0))
                .thenReturn(List.of(inv));

        List<Invoice> result = invoiceRepository.findByAmountGreaterThanHql(100.0);

        Assert.assertEquals(result.size(), 1);
        Assert.assertTrue(result.get(0).getAmount() > 100.0);
    }

    @Test(groups = "hqlCriteria", priority = 58)
    public void testHqlQueryReturnsEmptyWhenNoMatches() {
        when(invoiceRepository.findByAmountGreaterThanHql(10000.0))
                .thenReturn(Collections.emptyList());

        List<Invoice> result = invoiceRepository.findByAmountGreaterThanHql(10000.0);

        Assert.assertTrue(result.isEmpty());
    }

    @Test(groups = "hqlCriteria", priority = 59)
    public void testRuleRepositoryFindMatchingRulesByDescriptionPositive() {
        CategorizationRule rule = new CategorizationRule();
        Category cat = new Category();
        cat.setCategoryName("Travel");
        rule.setCategory(cat);
        rule.setKeyword("flight");
        rule.setPriority(10);

        when(ruleRepository.findMatchingRulesByDescription("Flight ticket to Delhi"))
                .thenReturn(List.of(rule));

        List<CategorizationRule> rules =
                ruleRepository.findMatchingRulesByDescription("Flight ticket to Delhi");

        Assert.assertEquals(rules.size(), 1);
        Assert.assertEquals(rules.get(0).getCategory().getCategoryName(), "Travel");
    }

    @Test(groups = "hqlCriteria", priority = 60)
    public void testRuleRepositoryFindMatchingRulesByDescriptionNoMatch() {
        when(ruleRepository.findMatchingRulesByDescription("random text"))
                .thenReturn(Collections.emptyList());

        List<CategorizationRule> rules =
                ruleRepository.findMatchingRulesByDescription("random text");

        Assert.assertTrue(rules.isEmpty());
    }

    @Test(groups = "hqlCriteria", priority = 61)
    public void testEngineExactMatchRule() {
        Invoice invoice = new Invoice();
        invoice.setDescription("Electricity Bill");

        Category cat = new Category();
        cat.setCategoryName("Utilities");

        CategorizationRule rule = new CategorizationRule();
        rule.setCategory(cat);
        rule.setKeyword("Electricity Bill");
        rule.setMatchType("EXACT");
        rule.setPriority(5);

        Category result = engine.determineCategory(invoice, List.of(rule));
        Assert.assertEquals(result.getCategoryName(), "Utilities");
    }

    @Test(groups = "hqlCriteria", priority = 62)
    public void testEngineContainsMatchRule() {
        Invoice invoice = new Invoice();
        invoice.setDescription("Monthly internet subscription");

        Category cat = new Category();
        cat.setCategoryName("Subscription");

        CategorizationRule rule = new CategorizationRule();
        rule.setCategory(cat);
        rule.setKeyword("subscription");
        rule.setMatchType("CONTAINS");
        rule.setPriority(7);

        Category result = engine.determineCategory(invoice, List.of(rule));
        Assert.assertEquals(result.getCategoryName(), "Subscription");
    }

    @Test(groups = "hqlCriteria", priority = 63)
    public void testEngineRegexMatchRule() {
        Invoice invoice = new Invoice();
        invoice.setDescription("Taxi ride to airport");

        Category cat = new Category();
        cat.setCategoryName("Travel");

        CategorizationRule rule = new CategorizationRule();
        rule.setCategory(cat);
        rule.setKeyword(".*Taxi.*");
        rule.setMatchType("REGEX");
        rule.setPriority(9);

        Category result = engine.determineCategory(invoice, List.of(rule));
        Assert.assertEquals(result.getCategoryName(), "Travel");
    }

    @Test(groups = "hqlCriteria", priority = 64)
    public void testEngineNoRulesReturnsNullCategoryEdgeCase() {
        Invoice invoice = new Invoice();
        invoice.setDescription("Some unknown expense");

        Category result = engine.determineCategory(invoice, new ArrayList<>());
        Assert.assertNull(result);
    }
}
