package msa.edgeservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableZuulProxy
public class EdgeServiceApplication {
    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

	public static void main(String[] args) {
		SpringApplication.run(EdgeServiceApplication.class, args);
	}
}

@RestController
class QuoteController{
    @Autowired
    RestTemplate restTemplate;

    //@Value("${quote}")
    @Value("Hi this is the default message")
    private String defaultQuote;

    @RequestMapping("quoterama")
    @HystrixCommand(fallbackMethod = "getDefaultMethod")
    public String getRandomQuote(){
        return restTemplate.getForObject("http://quote-service/random", String.class);
    }

    public String getDefaultMethod() {
        return defaultQuote;
    }
}
