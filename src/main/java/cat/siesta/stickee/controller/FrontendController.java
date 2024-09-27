package cat.siesta.stickee.controller;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.service.NoteService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FrontendController {

	@Autowired
	StickeeConfig stickeeConfig;

	@Autowired
	NoteService noteService;

	@Value("${stickee.version}")
	private String version;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("notesBasePath", stickeeConfig.getBasePath());
		model.addAttribute("notesMaxExpirationTime",
				DurationFormatUtils.formatDurationWords(stickeeConfig.getMaxExpirationTime().toMillis(), true,
						true));
		model.addAttribute("expirationTimes", stickeeConfig.getExpirationTimes().stream()
				.collect(Collectors.toMap(
						et -> et,
						et -> DurationFormatUtils.formatDurationWords(et.toMillis(), true, true),
						(existing, replacement) -> existing,
						LinkedHashMap::new)));
		model.addAttribute("version", version);
		return "index";
	}

	// TODO: make cached
	@GetMapping("/${notes.base-path}/detail/{id}")
	public String detail(@PathVariable("id") String id, Model model, HttpServletResponse response) {
		return noteService.get(id).map(note -> {
			model.addAttribute("notesBasePath", stickeeConfig.getBasePath());
			model.addAttribute("noteText", note.getText());
			model.addAttribute("noteId", id);
			model.addAttribute("noteExpirationTimestamp",
					note.getExpirationTimestamp().format(DateTimeFormatter.ofPattern("d MMM uuuu, HH:mm")));
			return "detail";
		}).orElseGet(() -> {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return "not_found";
		});
	}
}
