SWFDecoder{
	classvar dec_matrix;

	*new{|encoded_signal|
		^super.new.init(encoded_signal);
	}

	init{|encoded_signal|
		var result;

		if(encoded_signal.size == 6, {dec_matrix = this.loadMatrix("/swf/wavelets_aranyo_0-universal-wv-remTrue-Enorm.txt")}, {});
		if(encoded_signal.size == 18, {dec_matrix = this.loadMatrix("/swf/wavelets_aranyo_1-universal-wv-remTrue-Enorm.txt")}, {});
		dec_matrix.postln;
		encoded_signal.postln;
		result = this.matmul(dec_matrix, encoded_signal);
		^result;
	}

	loadMatrix{|directory|
		var file, matrix;
		file = FileReader.read(Platform.userExtensionDir ++ directory);
		matrix = Array.fill2D(file.size, file[0].size,{
			arg r, c;
			file[r][c].asFloat;
		});
		^matrix;
	}

	matmul{|mat1, mat2|
		var result;
		result = Array.fill(mat1.size,{
			arg i;var sum = 0;
			for(0,mat2.size-1,{
				arg j;
				sum = sum + (mat1[i][j] * mat2[j]);
			});
			sum;
		});
		^result;
	}
}