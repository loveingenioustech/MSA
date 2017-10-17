package msa.quoteservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
public class QuoteServiceApplication {
	@Bean
	@Autowired
	CommandLineRunner commandLineRunner(QuoteRepository quoteRepository){
		return args -> {
			quoteRepository.save(new Quote("Clothing", "UK"));
			quoteRepository.save(new Quote("Shoes", "China"));
			quoteRepository.save(new Quote("Bags", "Italy"));

			quoteRepository.findAll().forEach(System.out::println);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(QuoteServiceApplication.class, args);
	}
}

@RepositoryRestResource
interface QuoteRepository extends CrudRepository<Quote, Long>{
    @Query("select q from Quote q order by RAND()")
    List<Quote> getQuotesRandomOrder();
}

@RestController
class QuoteController{
    @Autowired
    QuoteRepository quoteRepository;

    @RequestMapping("/random")
    public Quote getRandomQuote(){
        return quoteRepository.getQuotesRandomOrder().get(0);
    }
}

@Entity
class Quote{
	@Id
	@GeneratedValue
	private Long id;
	private String text;
	private String source;

	public Quote() { // JPA & JSON
	}

	public Quote(String text, String source) {
		this.text = text;
		this.source = source;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "Quote{" +
				"id=" + id +
				", text='" + text + '\'' +
				", source='" + source + '\'' +
				'}';
	}
}
