package com.navinfo.opentsp.platform.location.kit.lang;

import java.util.ArrayList;
import java.util.List;

public class BytesArray {
	private List<byte[]> _arrays;
	private int _arrays_length;

	public BytesArray() {
		if(this._arrays == null){
			this._arrays = new ArrayList<byte[]>();
			this._arrays_length = 0;
		}
	}
	public BytesArray append(byte[] bytes){
		this._arrays.add(bytes);
		this._arrays_length +=bytes.length;
		return this;
	}
	public byte[] get(){
		byte[] bytes = new byte[this._arrays_length];
		int index = 0;
		for (byte[] b : this._arrays) {
			ArraysUtils.arrayappend(bytes, index, b);
			index += b.length;
		}
		return bytes;
	}
}
