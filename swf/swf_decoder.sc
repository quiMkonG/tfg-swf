SWFDecoder : SWF{
	classvar dec_matrix;

	*new{|encoded_signal|
		^super.new.init(encoded_signal);
	}

	init{|encoded_signal|
		var result;

		if(encoded_signal.size == 6, {dec_matrix = this.loadMatrix("/swf/swf-auxiliar-matrices/wavelets_aranyo_0-universal-wv-remTrue-Enorm.txt")}, {});
		if(encoded_signal.size == 18, {dec_matrix = this.loadMatrix("/swf/swf-auxiliar-matrices/wavelets_aranyo_1-universal-wv-remTrue-Enorm.txt")}, {});
		result = this.matmul(dec_matrix, encoded_signal);
		^result;
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