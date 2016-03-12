package com.ownxile.rs2.items;

import com.ownxile.core.World;
import com.ownxile.util.file.ListFile;

public class DonatorItem extends ListFile {

	private int id;

	public DonatorItem(int id, String filePath) {
		super(filePath);
		this.setId(id);
		World.donatorItems.add(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
