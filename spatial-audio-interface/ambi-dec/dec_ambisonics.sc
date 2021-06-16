DecAmbisonics{

	*ar{|in, order|
		var result, dec_matrix, path;
		path = "/spatial-audio-interface/ambi-dec/ambisonics_aranyo_" ++ order ++ "-universal-3D-remTrue-Enorm.txt";
		dec_matrix = this.loadMatrix(path).flop;
		result = this.matmul(in, dec_matrix);
		^result;
	}

	*loadMatrix{|directory|
		var file, matrix;
		file = FileReader.read(Platform.userExtensionDir ++ directory, true);
		matrix = Array.fill2D(file.size, file[0].size,{
			arg r, c;
			file[r][c].asFloat;
		});
		^matrix;
	}

	*matmul{|mat1, mat2|
		var result;

		result = Array.fill(mat2[0].size,{
			arg i;var sum = 0;
			for(0,mat1.size-1,{
				arg j;
				sum = sum + (mat1[j] * mat2[j][i]);
			});
			sum;
		});
		^result;
	}
}
