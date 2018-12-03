/**
 * 
 */
package ecommerce.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.model.Product;
import ecommerce.service.ProductService;

/**
 * @author Ravindar
 *
 */
@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/addProduct", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addProduct(@RequestBody Product product) {
		return productService.addProduct(product);

	}

	@RequestMapping(value = "/getProductInfo", method = RequestMethod.POST)
	public JSONObject getProductInfo(@RequestParam(value = "id", required = true) String id) {
		return productService.getProductInfo(id);

	}

	@RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
	public JSONObject updateProduct(@RequestBody Product product) {
		return productService.updateProduct(product);

	}

	@RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
	public JSONObject deleteProduct(@RequestParam(value = "id", required = true) String id) {
		return productService.deleteProduct(id);

	}
}
