/**
 * 
 */
package net.combase.scale.web;

import net.combase.scale.ScaleConnector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author "Till Freier"
 *
 */
@Controller
@RequestMapping(value = "/")
public class WebController
{
	@Autowired
	private ScaleConnector scaleConnector;

	@RequestMapping(value = "/w", method = RequestMethod.GET, produces = "text/html")
	@ResponseBody
	public String readWeight(@RequestParam(value = "index", required = false) String index)
	{
		System.out.println("set weight for index " + index);
		scaleConnector.refreshWeight();

		try
		{
			Thread.sleep(350);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		int popWeight = scaleConnector.popWeight();

		if (popWeight < 0)
			popWeight = 0;

		System.out.println("return weight: " + popWeight);

		return String.valueOf(popWeight);
	}

}
