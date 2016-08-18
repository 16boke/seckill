package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	private Logger logger = Logger.getLogger(SeckillController.class);
	@Autowired
	private SeckillService seckillServer;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		System.out.println("进入到list...");
		List<Seckill> list = this.seckillServer.getSeckillList();
		System.out.println(list);
		model.addAttribute("list", list);
		return "list";// WEB-INF/jsp/"list".jsp
	}

	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillServer.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}

	/**
	 * ajax 返回 json
	 * 
	 * @param seckillId
	 */
	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillServer.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			result = new SeckillResult<Exposer>(false, ex.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,
			@CookieValue(value = "killPhone", required = false) Long phone) {
		if (phone == null) {
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
		SeckillResult<SeckillExecution> result;
		try {
			//SeckillExecution seckillExecution = seckillServer.executeSeckill(seckillId, phone, md5);
			//采用存储过程来实现秒杀
			SeckillExecution seckillExecution = seckillServer.executeSeckillProcedure(seckillId, phone, md5);
			result = new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (RepeatKillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
			result = new SeckillResult<SeckillExecution>(true, execution);
		}
		return result;
	}

	@RequestMapping(value = "/time/now", method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time() {
		Date now = new Date();
		System.out.println(now);
		System.out.println(new SeckillResult<Long>(true, now.getTime()));
		return new SeckillResult<Long>(true, now.getTime());
	}
}
