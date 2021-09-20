package jp.co.example.form;

import org.springframework.web.multipart.MultipartFile;

public class RegisterItemForm {
	private String name;
	private String description;
	private String priceM;
	private String priceL;
	private MultipartFile imageFile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriceM() {
		return priceM;
	}

	public void setPriceM(String priceM) {
		this.priceM = priceM;
	}

	public String getPriceL() {
		return priceL;
	}

	public void setPriceL(String priceL) {
		this.priceL = priceL;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	@Override
	public String toString() {
		return "RegisterItemForm [name=" + name + ", description=" + description + ", priceM=" + priceM + ", priceL="
				+ priceL + ", imageFile=" + imageFile + "]";
	}

}
