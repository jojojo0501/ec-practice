package jp.co.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.domain.Topping;
import jp.co.example.repository.ToppingRepository;

@Service
@Transactional
public class ToppingService {

	@Autowired
	private ToppingRepository toppingRepository;
	
	/**
	 * 全トッピング情報を取得します.
	 * @return　トッピング情報
	 */
	public List<Topping> searchAll(){
		List<Topping> toppingList = toppingRepository.findAll();
		return toppingList;
	}
}
