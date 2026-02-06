package kr.co.kpcard.ktree.controller;

import kr.co.kpcard.ktree.app.enums.Position;
import kr.co.kpcard.ktree.app.enums.AuthLevel;
import kr.co.kpcard.ktree.domain.Employe;
import kr.co.kpcard.ktree.service.DivisionService;
import kr.co.kpcard.ktree.service.TeamService;
import kr.co.kpcard.ktree.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/employe")
@RequiredArgsConstructor
public class EmployeController {

    private final Logger logger = LoggerFactory.getLogger(EmployeController.class);
    private final EmployeService employeService;
    private final DivisionService divisionService;
    private final TeamService teamService;

    @GetMapping("/form")
    public String showEmployeForm(@RequestParam(value = "seq", required = false) Long seq, Model model) {
        logger.info("showEmployeForm | IN | seq: {}", seq);
        Employe employe = new Employe();
        String mode = "add";
        if (seq != null) {
            mode = "edit";
            employe = employeService.getEmploye(seq);
            if (employe == null) {
                // Handle case where employee is not found, e.g., redirect to error or list page
                logger.warn("Employe with SEQ {} not found for editing.", seq);
                return "redirect:/employe/list"; // Assuming an employe list page exists
            }
            logger.info("name : {}", employe.getName());
        }
        model.addAttribute("employe", employe);
        model.addAttribute("mode", mode);
        model.addAttribute("divisions", divisionService.getDivisionList());
        model.addAttribute("teams", teamService.getTeamList(0));
        model.addAttribute("authLevels", AuthLevel.values());
        model.addAttribute("positions", Position.values());
        logger.info("showEmployeForm | OUT | Adding employe to model: {}", employe);
        return "sub/employe/employeAddEdit";
    }

    @PostMapping("/save")
    public String saveEmploye(@ModelAttribute Employe employe,
            RedirectAttributes redirectAttributes) {
        logger.info("saveEmploye | IN | mode : {}, employe: {}", employe.getMode(), employe);
        boolean success;
        String message;

        if (employe.getMode() != null && employe.getMode().equals("edit")) {
            // Update existing employee
            logger.info("trying to update employe: {}", employe);
            success = employeService.updateEmploye(employe);
            message = success ? "Employee updated successfully!" : "Failed to update employee!";
        } else {
            // Add new employee
            logger.info("trying to add employe: {}", employe);
            success = employeService.addEmploye(employe);
            message = success ? "Employee added successfully!" : "Failed to add employee!";
        }

        redirectAttributes.addFlashAttribute("message", message);
        logger.info("saveEmploye | OUT | message: {}", message);
        return "redirect:/employe/list"; // Redirect to employee list page after save
    }

    @GetMapping("/list")
    public String employeList(@RequestParam(required = false) String employeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer divisionCode,
            @RequestParam(required = false) String useYn,
            Model model) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeId", employeId);
        params.put("name", name);
        params.put("divisionCode", divisionCode);
        params.put("useYn", useYn);

        model.addAttribute("employees", employeService.getEmployeList(params));
        model.addAttribute("divisions", divisionService.getDivisionList());
        model.addAttribute("param", params);
        logger.info("employeList | OUT | params: {}", model.getAttribute("param"));
        return "sub/employe/employeList";
    }
}
