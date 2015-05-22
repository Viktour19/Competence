
package com.n4labs.competence;

import java.util.List;

public class Pixabay{
   	private List hits;
   	private Number totalHits;

 	public List getHits(){
		return this.hits;
	}
	public void setHits(List hits){
		this.hits = hits;
	}
 	public Number getTotalHits(){
		return this.totalHits;
	}
	public void setTotalHits(Number totalHits){
		this.totalHits = totalHits;
	}
}
