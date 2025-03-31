package model.logic;

import javax.ejb.Stateless;

@Stateless
public class DummySendStringViaEmailLogic {
	public boolean execute(String sendString, String gakusekiBango) {
		return true;
	}
}
