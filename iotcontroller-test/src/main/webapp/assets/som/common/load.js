
function loadProgressBar() {
	try	{
               if (top.frames && top.frames.length>1) {
			if (top.frames[0] && top.frames[0].frames && top.frames[0].frames.length>2) {
				if (top.frames[0].frames[2] && top.frames[0].frames[2].hide) {
					top.frames[0].frames[2].hide();
				}
			}
		}
	} catch(e) {
	  	//alert("error name: " + e.name);
	}
}

function unloadProgressBar() {
	try	{
            if (top.frames && top.frames.length>1) {
			if (top.frames[0] && top.frames[0].frames && top.frames[0].frames.length>2) {
				if (top.frames[0].frames[2] && top.frames[0].frames[2].show) {
					top.frames[0].frames[2].show();
				}
			}
		}
	} catch(e) {
	  	//alert("error name: " + e.name);
	}

}





