package org.plugin.service.sync.impl.music163.model.song;

public class H{
	private Integer br;
	private Integer fid;
	private Integer size;
	private Integer vd;
	private Integer sr;

	public Integer getBr(){
		return br;
	}

	public void setBr(Integer br){
		this.br = br;
	}

	public Integer getFid(){
		return fid;
	}

	public void setFid(Integer fid){
		this.fid = fid;
	}

	public Integer getSize(){
		return size;
	}

	public void setSize(Integer size){
		this.size = size;
	}

	public Integer getVd(){
		return vd;
	}

	public void setVd(Integer vd){
		this.vd = vd;
	}

	public Integer getSr(){
		return sr;
	}

	public void setSr(Integer sr){
		this.sr = sr;
	}

	@Override
 	public String toString(){
		return 
			"H{" + 
			"br = '" + br + '\'' + 
			",fid = '" + fid + '\'' + 
			",size = '" + size + '\'' + 
			",vd = '" + vd + '\'' + 
			",sr = '" + sr + '\'' + 
			"}";
		}
}
