SWF{
	*ar{|in, azi, ele, level|
		var enc, dec;
		enc = in * this.encode(azi, ele, level);
		//aqui hi va algo
	}

	*encode{|azi, ele, level|
		var result;
		result = SWFEncoder.new();
		^result.getCoarses(azi, ele, level);
	}

	*decode{|azi, ele, level|
		var result;
		result = SWFDecoder.new();
		^result.getCoarses(azi, ele, level);
	}
}