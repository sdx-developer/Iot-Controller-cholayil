(function ($) {

    $.fn.createTable = function (data, options) {

        var element = this;
        var settings = $.extend({}, $.fn.createTable.defaults, options);
        var selector;

        if (element[0].className !== undefined) {
            var split = element[0].className.split(' ');
            selector = '.' + split.join('.') + ' ';
        } else if (element[0].id !== undefined) {
            selector = '#' + element[0].id + ' ';
        }

        //var table = '<table class="json-to-table">';
		var table = '<table id="'+options.tblRepId+'" role="'+options.idColumnType+'" data="'+options.dataAttr+'" class="'+options.tblClass+'" style="'+options.styleAttr+'">';

        table += '<thead><th style="max-width:40px;">S.No</th>';
        table += $.fn.createTable.parseTableData(data, true, options);
        table += '</thead>';
        table += '<tbody>';
        table += $.fn.createTable.parseTableData(data, false, options);
        table += '</tbody>';
        table += '</table>';

        element.html(table);

        return function () { }();
    };

    $.fn.createTable.getHighestColumnCount = function (data) {
        var count = 0, temp = 0, column = {max: 0, when: 0};
        for (var i = 0; i < data.length; i++) {
            count = $.fn.getObjectLength(data[i]);
            if (temp <= count) {
                temp = count;
                column.max = count;
                column.when = i;
            }
        }
        return column;
    };

    $.fn.createTable.parseTableData = function (data, isTHead, options) {
        var row = '';
        for (var i = 0; i < data.length; i++) {
            if (isTHead === false) {
				row += '<tr><td>' + (i + 1) + '</td>';				
			}
            $.each(data[i], function (key, value) {
                if (isTHead === true) {
                    if (i === $.fn.createTable.getHighestColumnCount(data).when) {
						if (options.idColumnType!=null && options.idColumnData === key){
							row += '<th>Select</th>';
						} else {
							row += '<th>' + $.fn.humanize(key) + '</th>';
						}
                    }
                } else if (isTHead === false) {
					if (options.idColumnType!=null){
						if (options.idColumnData === key){
							if (options.idColumnType === mulSelType){
								row += $.fn.buildMultiSelector(data, value, options.dataAttr);
							} else if (options.idColumnType === sinSelType){
								row += '<td role="rbSelector">' + value + '</td>';
							} else {
								row += '<td>' + value + '</td>';
							}
								
							
						} else {
							row += '<td>' + value + '</td>';
						}
					} else {
						
					}
                }
            });
            if (isTHead === false) {
				row += '</tr>';
			}				
        }
        return row;
    };
	
	$.fn.buildMultiSelector = function (data, value, dataAttr) {
		var retTbl = '<td><div class="jui jennifer item">';
		retTbl += '<input data="'+dataAttr+'" type="checkbox" value="'+value+'">';
		retTbl += '</div></td>';
		return retTbl;
	}

    $.fn.getObjectLength = function (object) {
        var length = 0;
        for (var key in object) {
            if (object.hasOwnProperty(key)) {
                ++length;
            }
        }
        return length;
    };

    $.fn.humanize = function (text) {
        var string = text.split('_');
        for (i = 0; i < string.length; i++) {
            string[i] = string[i].charAt(0).toUpperCase() + string[i].slice(1);
        }
        return string.join(' ');
    };

    $.fn.createTable.defaults = {
		tblRepId : "j2lId",
		dataAttr : "tempData",
		styleAttr: "width:100%;",
		tblClass : "table classic",
		idColumnType : null,
		idColumnData : null
    }

}(jQuery));