package com.bcpr.backend.papago;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bcpr.backend.ocr.OCRMapper;
import com.bcpr.backend.ocr.domain.Media_Trans;
import com.bcpr.backend.security.api.UserResource;
import com.bcpr.backend.security.domain.User;
import com.bcpr.backend.security.service.UserService;
import com.bcpr.backend.utill.FileSaveHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
//papago api 사용
public class PapagoController {
	
	private PapagoMapper mapper;
	
	@Autowired
	public void setMapper(PapagoMapper mapper) {
		this.mapper = mapper;
	}
	
	//Papago api 사용
	@PostMapping("/papago/array")
	 public String papaForArray(	
			 @RequestParam(value = "text[]") List<String> text,
			 @RequestParam(value = "from_language") String from_language,
			 @RequestParam(value = "to_language") String to_language,
				HttpServletRequest request) throws IOException{
			
		PapagoRepo papagorepo = new PapagoRepo();
		String t = papagorepo.translationForArray(text, from_language, to_language);
		papagorepo.setText(t);
	    return papagorepo.getText();
	}
	
	@PostMapping("/papago/json")
	 public String papaForJson(	
			 @RequestParam(value = "text") String text,
			 @RequestParam(value = "from_language") String from_language,
			 @RequestParam(value = "to_language") String to_language,
				HttpServletRequest request) throws IOException{
			
		PapagoRepo papagorepo = new PapagoRepo();
		String t = papagorepo.translation(text, from_language, to_language);
		papagorepo.setText(t);

	    return papagorepo.getText();

	}
	//TRANSLATION 보관함에 저장
	@PostMapping("/papago/upload")
	public int upload(
			@RequestParam("email") String email,
			@RequestParam("trans_date") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime trans_date,
			@RequestParam("input") String input,
			@RequestParam("output") String output,
			HttpServletRequest request)throws IOException {

		return mapper.insertTranslation_TranslaterContent(email, trans_date, input, output);
	}
	
	//media_trans 보관함 아이템 불러오기 email 기준 전부
	@GetMapping("/papago/download")
	public List<Translation> download(
			@RequestParam("email") String email){
		//log.info("test : {}",test.get(0));
		return mapper.getTranslation(email);
	}
	
	//media_trans 보관함 아이템 삭제 email, no 기준(협의필요)
	@PostMapping("/papago/delete")
	public int delete(
			@RequestParam("email") String email,
			@RequestParam("translation_no") int translation_no) {
		return mapper.deleteTranslation_TranslaterContent(email, translation_no);
	}
}
