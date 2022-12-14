package by.tms.web.controller;

import by.tms.entity.AbstractProduct;
import by.tms.entity.Customer;
import by.tms.entity.Store;
import by.tms.entity.User;
import by.tms.service.CustomerService;
import by.tms.service.ProductService;
import by.tms.service.StoreService;
import by.tms.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainController {
    private final StoreService storeService;
    private final CustomerService customerService;
    private final ProductService productService;

    @Autowired
    @Qualifier("inMemoryProductStorage")
    private Storage<AbstractProduct, Long> productStorage;

    public MainController(StoreService storeService, CustomerService customerService, ProductService productService) {
        this.storeService = storeService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("user") User user) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                        HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        Optional<Store> store = storeService.findStoreByEmail(user.getEmail());
        Optional<Customer> customer = customerService.findCustomerByEmail(user.getEmail());
        if (store.isPresent()) {
            if (store.get().getPassword().equals(user.getPassword())) {
                session.setAttribute("currentUser", store.get());
                return "redirect:/";
            } else {
                model.addAttribute("message", "Wrong password");
                return "login";
            }
        } else if (customer.isPresent()) {
            if (customer.get().getPassword().equals(user.getPassword())) {
                session.setAttribute("currentUser", customer.get());
                return "redirect:/";
            } else {
                model.addAttribute("message", "Wrong password");
                return "login";
            }
        } else {
            model.addAttribute("message", "No such user");
            return "login";
        }
    }

    @GetMapping("/mobile")
    public String mobile(Model model) {
        model.addAttribute("smartphones", productService.getSmartphoneList());
        return "mobileCatalog";
    }

    @GetMapping("/tabletpc")
    public String tabletPC() {
        return "tabletpcCatalog";
    }

    @GetMapping("/ebook")
    public String ebook() {
        ;
        return "ebookCatalog";
    }

    @GetMapping("/smartwatch")
    public String smartwatch() {
        return "smartwatchCatalog";
    }

    @GetMapping("/notebook")
    public String notebook() {
        return "notebookCatalog";
    }

}