package by.tms.web.controller;

import by.tms.entity.Store;
import by.tms.service.CustomerService;
import by.tms.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/store")
public class OnlineStoreController {
    private final StoreService storeService;
    private final CustomerService customerService;

    public OnlineStoreController(StoreService storeService, CustomerService customerService) {
        this.storeService = storeService;
        this.customerService = customerService;
    }

    @GetMapping("/storeRegistration")
    public String storeRegistration(@ModelAttribute("newStore") Store store) {
        return "storeRegistration";
    }

    @PostMapping("/storeRegistration")
    public String registration(@Valid @ModelAttribute("newStore") Store store, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "storeRegistration";
        }
        if (storeService.findStoreByEmail(store.getEmail()).isEmpty()
                && customerService.findCustomerByEmail(store.getEmail()).isEmpty()) {
            storeService.saveStore(store);
            return "redirect:/";
        } else {
            model.addAttribute("message", "User already exists");
            return "storeRegistration";
        }
    }
}
