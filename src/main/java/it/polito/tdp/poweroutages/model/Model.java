package it.polito.tdp.poweroutages.model;

import java.awt.im.InputMethodHighlight;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	private List<PowerOutages> soluzione;
	PowerOutageDAO podao;
	private double max;
	private List<PowerOutages> listaPowerByNerc;


	public Model() {
		podao = new PowerOutageDAO();
	}

	public List<Nerc> getNercList() {
		return podao.getNercList();
	}

	public List<PowerOutages> seleziona(int nercId,int anno,int ore){
		listaPowerByNerc=this.podao.getPowerByNerc(nercId);
		List<PowerOutages>parziale=new ArrayList<PowerOutages>();
		max=0.0;
		seleziona_ricorsiva(parziale,anno,ore);
		return soluzione;

	}

	private void seleziona_ricorsiva(List<PowerOutages>parziale, int anno,int ore) {
		if(parziale.size()!=0) {
			if(parziale.size()>listaPowerByNerc.size())
				return;
			if(calcolaAnni(parziale)>anno && parziale.size()!=0)
				return;
			if(calcolaOre(parziale)>ore && parziale.size()!=0)
				return;
			if(calcolaOre(parziale)<=ore && parziale.size()!=0 && calcolaAnni(parziale)<=anno) {
				double personeCoinvolte=calcolaPersoneCoinvolte(parziale);
				if(personeCoinvolte>max) {
					max=personeCoinvolte;
					soluzione=new ArrayList<PowerOutages>(parziale);
				}
			}
		}


		for(PowerOutages p:listaPowerByNerc) {
			if(!parziale.contains(p)) {
				boolean b=false;
				if(parziale.size()==0) {
					parziale.add(p);
					b=true;
				}
				else {
					if(isPowerValid(p,parziale, anno)) {
						parziale.add(p);
						b=true;
					}
				}
				if(b==true) {
					seleziona_ricorsiva(parziale, anno, ore);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		return;
	}


	private int calcolaPersoneCoinvolte(List<PowerOutages> parziale) {
		int tot=0;
		for(PowerOutages p:parziale) {
			tot+=p.getCustomersAffected();
		}
		return tot;
	}


	private boolean isPowerValid(PowerOutages p,List<PowerOutages> parziale, int anno) {
		if(p.getDateEventBegan().getYear()-parziale.get(0).getDateEventBegan().getYear()<=anno && !parziale.contains(p))
			return true;
		else 
			return false;

	}
	public double calcolaOre(List<PowerOutages> l) {
		double tot=0;
		for(PowerOutages p:l) {
			LocalDateTime tempDateTime=LocalDateTime.from(p.getDateEventBegan());
			long hours = tempDateTime.until(p.getDateEventFinished(), ChronoUnit.HOURS);
			tot+=hours;
		}
		return tot;
	}
	private double calcolaAnni(List<PowerOutages> l) {

		LocalDateTime tempDateTime=LocalDateTime.from(l.get(0).getDateEventBegan());
		long years = tempDateTime.until(l.get(l.size()-1).getDateEventFinished(), ChronoUnit.YEARS);
		return years;
	}

	public List<PowerOutages> getPowerByNerc(int nercId){
		return this.podao.getPowerByNerc(nercId);
	}
}
