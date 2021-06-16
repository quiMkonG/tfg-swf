SWF{
	*ar{|in, coarse|
		var enc, dec;
		enc = in * coarse;
		dec = SWFDecoder.new(enc);
		^dec;
	}

	loadMatrix{|directory|
		var file, matrix;
		file = FileReader.read(Platform.userExtensionDir ++ directory, true);
		matrix = Array.fill2D(file.size, file[0].size,{
			arg r, c;
			file[r][c].asFloat;
		});
		^matrix;
	}
}
