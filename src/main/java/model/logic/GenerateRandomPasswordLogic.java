package model.logic;

import java.util.Random;

import javax.ejb.Stateless;

@Stateless
public class GenerateRandomPasswordLogic {
	// 引用元：http://qiita.com/tool-taro/items/885592e66333d1e5fea3
	public String execute() {
		final int passwordLength = 16;
		StringBuilder password = new StringBuilder();
		StringBuilder passwordSource = new StringBuilder();

		//<!-- パスワードに使用可能な全ての文字を連結させてパスワードソースを作る
		// 数字
		for (int i = 0x30; i < 0x3A; i++) {
			passwordSource.append( (char)i );
		}
		// 記号
		for (int i = 0x21; i < 0x30; i++) {
			passwordSource.append( (char)i );
		}
		// アルファベット小文字
		for (int i = 0x41; i < 0x5b; i++) {
			passwordSource.append( (char)i );
		}
		// アルファベット大文字
		for (int i = 0x61; i < 0x7b; i++) {
			passwordSource.append( (char)i );
		}
		// -->

		//<!-- パスワード文字数だけ乱数を生成し、パスワードソースから使用文字を選択して連結する
		Random rand = new Random();
		while (password.length() < passwordLength) {
			// nextInt(int) を使うよりも、こっちの方が乱数が散らばるんですかね？
			int randomIndex = Math.abs( rand.nextInt() ) % passwordSource.length();
			password.append( passwordSource.charAt(randomIndex) );
		}
		// -->

		return password.toString();
	}
}
