package cat.siesta.stickee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cat.siesta.stickee.config.StickeeConfiguration;

@Controller
public class FrontendController {

	@Autowired
	StickeeConfiguration stickeeConfiguration;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("notesBasePath", stickeeConfiguration.getNotesBasePath());
		return "index";
	}

}
