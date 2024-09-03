package cat.siesta.stickee.controller;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cat.siesta.stickee.config.StickeeConfig;

@Controller
public class FrontendController {

	@Autowired
	StickeeConfig stickeeConfig;

	@Value("${stickee.version}")
	private String version;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("notesBasePath", stickeeConfig.getBasePath());
		model.addAttribute("notesMaxAge",
				DurationFormatUtils.formatDurationWords(stickeeConfig.getMaxAge().toMillis(), true, true));
		model.addAttribute("version", version);
		return "index";
	}

}
