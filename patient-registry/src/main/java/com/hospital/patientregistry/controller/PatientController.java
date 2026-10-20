package com.hospital.patientregistry.controller;

import com.hospital.patientregistry.model.Patient;
import com.hospital.patientregistry.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("total", service.countAll());
        model.addAttribute("admitted", service.countByStatus("ADMITTED"));
        model.addAttribute("outpatients", service.countByStatus("OUTPATIENT"));
        model.addAttribute("today", service.countRegisteredToday());
        model.addAttribute("recent", service.recent());
        return "dashboard";
    }

    @GetMapping("/patients")
    public String list(@RequestParam(defaultValue = "") String q,
                       @RequestParam(defaultValue = "") String status,
                       Model model) {
        model.addAttribute("patients", service.search(q, status));
        model.addAttribute("q", q);
        model.addAttribute("status", status);
        return "patients";
    }

    @GetMapping("/patients/new")
    public String newForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-form";
    }

    @PostMapping("/patients")
    public String create(@Valid @ModelAttribute("patient") Patient patient,
                         BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "patient-form";
        }
        service.save(patient);
        ra.addFlashAttribute("message", "Patient " + patient.getFullName() + " registered.");
        return "redirect:/patients";
    }

    @GetMapping("/patients/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient",
                service.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id)));
        return "patient-form";
    }

    @PostMapping("/patients/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("patient") Patient form,
                         BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "patient-form";
        }
        Patient saved = service.update(id, form)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));
        ra.addFlashAttribute("message", "Patient " + saved.getFullName() + " updated.");
        return "redirect:/patients";
    }

    @PostMapping("/patients/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "Patient record deleted.");
        return "redirect:/patients";
    }
}