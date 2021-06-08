SWF{
	*ar{|in, azi, ele, level|
		var enc, dec;
		enc = in * this.encode(azi, ele, level);
		dec = this.decode(enc);//aqui hi va algo
	}

	*encode{|azi, ele, level|
		var result;
		result = SWFEncoder.new();
		^result.getCoarses(azi, ele, level);
	}

	*decode{|encoded|
		var result;
		result = SWFDecoder.new(encoded);
		^result;
	}
}