var isIE = document.all?true:false;
var isTrue = "true";
var tempX = 0;
var tempY = 0;
var x ;
var y ;
var xOffset = 30;
var yOffset = -5;

function tweakAlphaFilter(ctrl){
	if(window.jQuery){
		alert("Yes jQ ctrl "+ctrl+", "+$("a['#alphaLinker']").attr("class"));
		var toHide = $(ctrl).hasClass("active");
		alert("toHide "+toHide);
		if (toHide){
			$(ctrl).removeClass("active");
			$("#filterA2Z").hide();
		} else {
			$(ctrl).addClass("active");
			$("#filterA2Z").show();	
		}
	} else {
		alert("NO jQ");
	}	
}

function closeNotify(){
	if(window.jQuery){
		$("#errortr").hide();
	} else {
	}
}

function isSpace(key) {
	var len = 0;
	for(var i = 0; i < key.length; i++) {
		if(key.charAt(i) == ' ') {
			len++;
		}
	}
	if(len == key.length) {
		return true;
	} else {
		return false;
	}
}

function openNewWindowUrl(loadURL,url) {
telaPopUp=window.open(loadURL, 'popup', 'width=800,height=500,scrollbars=yes');
    telaPopUp.moveTo(100,100);
    telaPopUp=window.open(url, 'popup', 'width=700,height=600,scrollbars=1');
    telaPopUp.moveTo(100,100);
}

function openPropertiesWindowUrl(form, url) {
    if (form.name == "subscriptionTemplate" || form.name == "subscription") {
        form.SOURCE.value = "";
    }
    if (url=='ProfileEditServlet?ActionType=ADVANCED'){
       telaPopUp=window.open(url, 'popup', 'width=680,height=600,titlebar=no,toolbar=no,menubar=no,scrollbars=yes');
       window.location.reload(true) ;
    } else {
       telaPopUp=window.open('common/Blank.html', 'popup', 'width=680,height=600,titlebar=no,toolbar=no,menubar=no,scrollbars=yes');
       telaPopUp.moveTo(100,100);
       form.action = url;
       form.target = "popup";
    }
       form.submit();
}

function openProductInfo(url) {
    telaInfoUp=window.open(url, 'productInfo', 'width=380,height=290');
    telaInfoUp.moveTo(100,100);
}

function openHelpWindowUrl(url) {
    telaHelpPopUp=window.open(url, 'helppopup', 'width=650,height=500,scrollbars=1');
    telaHelpPopUp.moveTo(100,100);
}

function closePopUps(){
	telaPopUp=window.open('','popup','height=2px,width=2px');
	telaPopUp.close();

	telaInfoUp=window.open('','productInfo','height=2px,width=2px');
	telaInfoUp.close();

	telaHelpPopUp=window.open('','helppopup','height=2px,width=2px');
	telaHelpPopUp.close();

	NewWindow=window.open('','awindow','height=2px,width=2px');
	NewWindow.close();

	telaCalendarPopUp=window.open('','calendarpopup','height=2px,width=2px');
	telaCalendarPopUp.close();

	tickerPopup=window.open('','tickerPopup','height=2px,width=2px');
	tickerPopup.close();

	window.close(self);

}
/**
* This functions validates for valid charactor in a given element.
* This method's arguments should be from object, defnValue followed by
* ------  [form field object and string value]
*/
function handleCommonDefnName(obj, defnValue) {
var returnVal = false;
if(defnValue.search("[^A-Za-z0-9_-]") != -1) {
   alert("Name only can contain Alphabets(A/a - Z/z) and Alpha Numeric(0-9) including underscore and Hyphan");
   obj.value = '';
   returnVal = true;
  }
  return returnVal;
}

function handleCommonField(obj, defnValue) {
var returnVal = false;
var field=obj.name;
if(defnValue.search("[^A-Za-z0-9_-]") != -1) {
   alert(field+" only can contain Alphabets(A/a - Z/z) and Alpha Numeric(0-9) including underscore and Hyphan");
   obj.value = '';
   returnVal = true;
  }
  return returnVal;
}

function handleCommonValue(obj, defnValue) {
    var returnVal = false;
    var chk =/^[-*\w ]+$/;
        if (!chk.test(defnValue)) {
        alert("Value only can contain Alphabets(A/a - Z/z) and Alpha Numeric(0-9) including underscore,Hyphen,* and white space");
        obj.value = '';
        returnVal = true;
    }
    return returnVal;
}

/*function handleCommonValue(obj, defnValue) {
    var returnVal = false;
    if ((defnValue.search("[^A-Za-z0-9_*-\W+ ]") != -1)) {
        alert("Value only can contain Alphabets(A/a - Z/z) and Alpha Numeric(0-9) including underscore,Hyphen,* and white space");
        obj.value = '';
        returnVal = true;
    }
    return returnVal;
}*/
function handleSubscriptionCommonValue(obj, defnValue) {
    var returnVal = false;
    if ((defnValue.search("[^A-Za-z0-9_* !]") != -1)) {
        alert("Value only can contain Alphabets(A/a - Z/z) and Alpha Numeric(0-9) including underscore, *, ! and white space");
        obj.value = '';
        returnVal = true;
    }
    return returnVal;
}

function handleHostNameValue(obj, defnValue) {
    var returnVal = false;
    if (defnValue.search("[^A-Za-z0-9._-]") != -1) {
        alert("Value only can contain Alphabets(A/a - Z/z) and Alpha Numeric(0-9) including underscore(_), dot(.) and hyphen(-)");
        obj.value = '';
        returnVal = true;
    }
    return returnVal;
}
/**
* This functions validates for valid charactor in a given element.
* This method's arguments should be from object, defnValue followed by
* ------  [form field object and string value]
*/

function handleCommonDefnName(obj, defnValue) {
var returnVal = false;
if(defnValue.search("[^A-Za-z0-9_-]") != -1) {
   alert("Name only can contain Alphabets(A/a - Z/z) and Alpha Numeric(0-9) including underscore and Hyphan");
   obj.value = '';
   returnVal = true;
  }
  return returnVal;
}

function setHandleEscapeCharactor(url) {
url = url.replace('%', "%25");
url = url.replace('#', "%23");
url = url.replace(/'/g, "\\\'");
url = url.replace(/"/g, '%22');
return url;
}

function setCancelEscapeCharactor(url) {
url = url.replace('%', "\%25");
url = url.replace('#', "%23");
url = url.replace(/'/g, "\\''");
url = url.replace(/"/g, '%22');
return url;
}


//------------------------------------------------------------------------------//
/*
*							FROM RELATED FUNCTIONS
*							----------------------
*/
//------------------------------------------------------------------------------//

// ---------------- << submitForm() >> ---------------- //
/**
* This functions submits the form using the parameters.
* The first argument is the form to be submitted,
* next is the sequence of hidden field and its value
* !NOTE:  This method's arguments should be from object followed by
* ------  [form field object and string value]
*/
function submitForm() {
	var args		= submitForm.arguments;
	var argLength	= args.length;
	if (argLength > 0) {
		var form = args[0];

		// skipping first argument since it is form object
		for (var i=1; i<argLength; i+=2) {

			// hidden_field.value = <value>
			args[i].value = args[i+1];
		}
		form.submit();
	}
}

// ---------------- << submitFormWithTarget() >> ---------------- //
/**
* This functions submits the form using the parameters.
* The first argument is the form to be submitted,
* second is the target,
* next is the sequence of hidden field and its value
* !NOTE:  This method's arguments should be from object, target object followed by
* ------  [form field object and string value]
*/
function submitFormWithTarget() {
	var args		= submitFormWithTarget.arguments;
	var argLength	= args.length;
	var form		= null;
	var target		= '_self';
	if (argLength > 0) {
		form = args[0];
		if (argLength > 1) {
			target = args[1];
			form.target = target;
		}

		// skipping first two argument since it is form object and target
		for (var i=2; i<argLength; i+=2) {

			// form_field.value = <value>
			args[i].value = args[i+1];
		}
		form.submit();
	}
}

// ---------------- << checkAll() >> ---------------- //
/**
* This functions checks or unchecks all the checkboxes in the form,
* using the value of allbox.
*
* Takes dynamic arguments.
* If 2 arguments
* 	1st argument - form object reference,
* 	2nd argument - checkall all checkbox reference
* If 3 arguments
* 	3rd argument - name of the checkboxes, that has to ckecked/unchecked
*/
function checkAll() {
	var args	= checkAll.arguments;
	if (args.length > 1) {
		var formObj		= args[0];
		var allBoxObj	= args[1];
		var	checkBoxName;
		var checkedVal	= allBoxObj.checked;
		if (args.length > 2) {
			checkBoxName = args[2];
		}
		for (var i=0; i<formObj.elements.length; i++) {
			if (formObj.elements[i].type == 'checkbox') {
				if (args.length == 2) {
					formObj.elements[i].checked = checkedVal;
				} else if (formObj.elements[i].name == checkBoxName) {
				    if(!formObj.elements[i].disabled){
					formObj.elements[i].checked = checkedVal;
					}
				}
			}
		}
	}
}


//------------------------------------------------------------------------------//
/*
*							VALIDATION FUNCTIONS
*							--------------------
*/
//------------------------------------------------------------------------------//

/**
* @author Sethu
* This functions validates for valid number in a given element.
*/

function isNumber(obj) {
    if((isNaN(obj.value)) || (((obj.value).indexOf('.')) != -1) || (((obj.value).indexOf('-')) != -1)){
        alert("Please enter a valid number");
        obj.value = '';
        obj.focus();
        return false;
    } else
    return true;
}

/**
* @author Sethu
* This functions validates for valid number in a given element.
*/

function isValidPositiveNumber(value) {
    if (isSpace(value)
	|| (isNaN(value))
	|| (((value).indexOf('.')) != -1)
	|| (((value).indexOf('-')) != -1)
	|| (value == 0)) {
        return false;
    } else {
	    return true;
	}
}

// ---------------- << compareDates() >> ---------------- //
/**
* This functions compares the date, month and year two given dates
* if first date is earlier than the second date, -1 is returned,
* if first date is later than the second date, 1 is returned and
* if the dates are same 0 is returned
* Assumption: None of the values is null and invalid
*/
function compareDates(d1, m1, y1, d2, m2, y2) {

	var date1	= new Number(d1);
	var month1	= new Number(m1);
	var year1	= new Number(y1);
	var date2	= new Number(d2);
	var month2	= new Number(m2);
	var year2	= new Number(y2);

	var	returnVal = 0;
	if (year1 < year2) {
		returnVal = -1;
	} else if (year1 > year2) {
		returnVal = 1;
	} else {
		//alert('same year');
		if (month1 < month2) {
			returnVal = -1;
		} else if (month1 > month2) {
			returnVal = 1;
		} else {
			//alert('same month');
			if (date1 < date2) {
				returnVal = -1;
			} else if (date1 > date2) {
				returnVal = 1;
			}
		}
	}

	return returnVal;

}

// ---------------- << set Focus() >> ---------------- //
/**
* This functions set the cursor state
* the formObj is the field
*/
function setFocus(formObj){
    formObj.focus();
}

// ---------------- << saveCurPos() >> ---------------- //
/**
* This functions stores the cursor position in the Text/TextArea object.
* The argument is the Text/TextArea object
*/
function saveCurPos(obj)
{
	if ( obj.isTextEdit ) {
		obj.caretPos = document.selection.createRange();
	}
}

// ---------------- << saveCurPos() >> ---------------- //
/**
* This functions inserts the value in the cursor position of the Text/TextArea object.
* The first argument is the Text/TextArea object
* The second argument is the values that has to be inserted
* finally saveCurPos() method should be called to reset the cursor position in the caretPos property of Text/TextArea
*/
function appendValue(obj, val) {
	var caretPos = obj.caretPos;
	caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == ' ' ? val + ' ' : val;
	obj.focus();
	saveCurPos(obj);
}

function textCounter(field, maxlimit) {
    if (field.value.length > maxlimit) {
        field.value = field.value.substring(0, maxlimit);
    }
}
// ---------------- << moveAll() >> ---------------- //
/**
* This functions moves the selected datas from one listbox object to the other
* The first argument is the ListBox object from which the datas should be moved.
* The second argument is the ListBox object to which the datas has be moved.
*/
function moveAll(fbox, tbox) {
	var temp = "";
    for (var i = 0; i < 25; i++) {
        temp = temp + " "
    }
    for(var x = 0;x<fbox.options.length;x++){
	if(fbox.options[x].selected && fbox.options[x].value != "sel"){
		var length = tbox.options.length;
		if(length == 1 && tbox.options[0].value == "sel") {
			length = 0;
                }
		tbox.options[length] = new Option(fbox.options[x].text,fbox.options[x].value,false,false);
		fbox.options[x] = null;
		x = -1;
	}
    }
    if(fbox.options.length == 0) {
	fbox.options[0]=new Option(temp,"sel",false,false)
    }
}

// layer validation in sd and nb

function ShowHint(tdObject, e, n, k) {
    var msgobj = document.getElementById(tdObject).innerHTML;
    var obj;
    var astyle;
    if (isTrue == "true") {
        x = getMouseX(e,n,k) - 35;
        y = getMouseY(e) - 15;
        isTrue = "false";

        showPopup(tdObject, e, x, y, k);
        //document.getElementById(tdObject).style.overflow = "scroll";
    }
    if (n > 180) {
       //document.getElementById(tdObject).style.overflow = "scroll";
    }
}

function ShowHint1(tdObject, e, n) {
    showPopup(tdObject, e, x, y)//ShowHint1(tdObject)
}
function HideHint() {
    isTrue = "true";
}

function getMouseX(e, n, k) {
    var width = screen.availWidth;
    if (isIE) {
        var pointX = event.screenX;
        var diff = width - pointX;
        if (k == 'properties') {
            if (diff >= 350) {
                tempX = event.clientX + document.body.scrollLeft;
            } else {
                tempX = (event.clientX + document.body.scrollLeft) - 315;
            }
        } else {
            if (diff >= 350) {
                if ((tempX + n) > width) {
                    tempX = (event.clientX + document.body.scrollLeft) - 200;
                } else {
                    tempX = (event.clientX + document.body.scrollLeft);
                }

            } else {
                tempX = (event.clientX + document.body.scrollLeft) - 200;
            }
        }
    } else {
        var pointX = e.screenX;
        var diff = width - pointX;
        if (k == 'properties') {
            if (diff >= 350) {
                tempX = e.pageX;
            } else {
                tempX = e.pageX - 315;
            }
        } else {
            if (diff >= 350) {
                tempX = e.pageX;
            } else {
                tempX = e.pageX - 200;
            }
        }
    }
    if (tempX < 0) {
        tempX = 0;
    }
    return tempX;
}

function getMouseY(e) {
    var height = screen.availHeight;
	if (isIE) {
        var pointY = event.screenY;
        var diff = height - pointY;
        if (diff >= 200) {
		tempY = event.clientY + document.body.scrollTop;
	} else {
            tempY = (event.clientY + document.body.scrollTop) - 100;
        }
    } else {
        var pointY = e.screenY;
        var diff = height - pointY;
        if (diff >= 200) {
		tempY = e.pageY;
        } else {
            tempY = e.pageY - 100;
	}
    }
    if (tempY < 0) {
        tempY = 0;
    }
	return tempY;
}

function showPopup (targetObjectId, eventObj, x, y, k) {
        if(eventObj) {
            hideCurrentPopup();
            eventObj.cancelBubble = true;
            var newXCoordinate = x;
            var newYCoordinate = y;
            moveObject(targetObjectId, newXCoordinate, newYCoordinate, k);
            if( changeObjectVisibility(targetObjectId, 'visible') ) {
                window.currentlyVisiblePopup = targetObjectId;
                return true;
            } else {
                return false;
            }
            } else {
            return false;
        }
    }

    function hideCurrentPopup() {
        if(window.currentlyVisiblePopup) {
        changeObjectVisibility(window.currentlyVisiblePopup, 'hidden');
        window.currentlyVisiblePopup = false;
        }
    }

    document.onclick = hideCurrentPopup;

    function initializeHacks() {
        if ((navigator.appVersion.indexOf('MSIE 5') != -1)
        && (navigator.platform.indexOf('Mac') != -1)
        && getStyleObject('blankDiv')) {
        window.onresize = explorerMacResizeFix;
        }
        resizeBlankDiv();
        createFakeEventObj();
    }

    function createFakeEventObj() {
        if (!window.event) {
        window.event = false;
        }
    }

    function resizeBlankDiv() {
        if ((navigator.appVersion.indexOf('MSIE 5') != -1)
        && (navigator.platform.indexOf('Mac') != -1)
        && getStyleObject('blankDiv')) {
        getStyleObject('blankDiv').width = document.body.clientWidth - 20;
        getStyleObject('blankDiv').height = document.body.clientHeight - 20;
        }
    }

    function explorerMacResizeFix () {
        location.reload(false);
    }

    function getStyleObject(objectId) {
        // cross-browser function to get an object's style object given its id
        if(document.getElementById && document.getElementById(objectId)) {
        // W3C DOM
        return document.getElementById(objectId).style;
        } else if (document.all && document.all(objectId)) {
        // MSIE 4 DOM
        return document.all(objectId).style;
        } else if (document.layers && document.layers[objectId]) {
        // NN 4 DOM.. note: this won't find nested layers
        return document.layers[objectId];
        } else {
        return false;
        }
    } // getStyleObject

    function changeObjectVisibility(objectId, newVisibility) {
        // get a reference to the cross-browser style object and make sure the object exists
        var styleObject = getStyleObject(objectId);
        if(styleObject) {
        styleObject.visibility = newVisibility;
        return true;
        } else {
        // we couldn't find the object, so we can't change its visibility
        return false;
        }
    } // changeObjectVisibility

    function moveObject(objectId, newXCoordinate, newYCoordinate, k) {
        // get a reference to the cross-browser style object and make sure the object exists
        var styleObject = getStyleObject(objectId);
        //alert("left");
        if(styleObject) {
            if (k == 'left') {
               styleObject.left = newXCoordinate - 280;
               styleObject.top = newYCoordinate;

            }  else {
                styleObject.left = newXCoordinate;
                styleObject.top = newYCoordinate;
            }
        return true;
        } else {
        // we couldn't find the object, so we can't very well move it
        return false;
        }
    } // moveObject

function validate(){
          return true;
     }

/*Mouse over jScript functions*/
function MM_swapImgRestore() { //v3.0
	  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
	}
	function MM_preloadImages() { //v3.0
	  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
	}
	function MM_findObj(n, d) { //v4.01
	  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	  if(!x && d.getElementById) x=d.getElementById(n); return x;
	}
	function MM_swapImage() { //v3.0
	  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}
