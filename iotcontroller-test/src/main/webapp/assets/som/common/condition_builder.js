
	var expression 		= "";
	var displayObject;
	var paretntDisplayObject;
	var originalExpression = "";
	var originalObject;
	var paretntOriginalObject;

	var formObjects;
	var formObjectValues;
	var variableName;
	var variableList;
	
	var expLen;
	var cursorPosition	= 0;
	var tempArray		= new Array(1);
	var expressionArray = new Array();

	cursorPosition = expressionArray.length;

	function setObjects(arg, display, original, parentDisplay, parentOriginal ) {
		expressionArray = arg;
		cursorPosition	= expressionArray.length;
		displayObject	= display;
		originalObject	= original;
		paretntDisplayObject	= parentDisplay;
		paretntOriginalObject 	= parentOriginal;
	}

	function setFormSettings(objArray, valArray) {
		formObjects = objArray;
		formObjectValues = valArray;
	}

	function setVariableList(varName, varArray) {
		variableName = varName;
		variableList = varArray;
	}

	function move(dir) {
		expLen = expressionArray.length;
		if (dir == 'LEFT' && cursorPosition > 0) {
			cursorPosition--;
		}
		if (dir == 'RIGHT' && cursorPosition < expLen ) {
			cursorPosition++;
		}
		setExpression(cursorPosition);
	}
	
	function insertLit(literal) {

		var args = insertLit.arguments;
		if (args.length > 1 && isSpace(args[0])) {
			alert(args[1]);
			return;
		}

		tempArray[0] = literal + ' ';
		var front = expressionArray.slice(0, cursorPosition);
		var rear = expressionArray.slice(cursorPosition, expLen);
		expressionArray = front.concat(tempArray, rear);
		cursorPosition++;
		setExpression(cursorPosition);
	}
	
	function deleteLit() {
		if (cursorPosition > 0) {
			expLen = expressionArray.length;
			var front = expressionArray.slice(0, cursorPosition-1);
			var rear = expressionArray.slice(cursorPosition, expLen);
			
			cursorPosition--;
			expressionArray = front.concat(rear);
			setExpression(cursorPosition);
		}
	}
	
	function setExpression(position) {

		expLen = expressionArray.length;
		expression = "";
		originalExpression = "";
		for (var i=0; i<expLen; i++) {
			if (position == i) {
				expression += ' | ';
			}
			expression += getVariableLabel(expressionArray[i]);
			originalExpression += expressionArray[i];
		}
		if ( (position == expLen) || (position == -1) ) {
			expression += ' | ';
		}
		displayObject.value = expression;
		originalObject.value = originalExpression;
		clearFormObjects();
	}

	function getVariableLabel(literal) {

		var litLen = literal.length;
		if (literal.substring(0, litLen + 2) == ('$' + variableName + '(') ) {
			var varVal;
			var varId = literal.substring(litLen + 2, literal.indexOf(')'));
			for (var i=0; i<variableList.length; i+=2) {
				if (varId == variableList[i]) {
					varVal = variableList[i+1];
				}
			}
			return '$' + variableName + '(' + varVal + ') ';
		}
		return literal;
	}

	function clearExpression() {
		cursorPosition = 0;
		expressionArray = new Array();
		setExpression(cursorPosition);
	}

	function clearFormObjects() {
		if (formObjects != null) {
			var formObjectsLength = formObjects.length;
			for (var i=0; i<formObjectsLength; i++) {
				formObjects[i].value = formObjectValues[i];
				if (i == 0) {
					formObjects[i].focus();
				}
			}
		}
	}

	function validate() {
		var validity = validateParantheses();
		if (validity) {
			alert('Expression is VALID');
		} else {
			alert('Parantheses Mismatch');
		}
	}

	function saveExpression() {
		var validity = validateParantheses();
		if (! validity) {
			alert('Parantheses Mismatch');
			return;
		}
		expression = "";
		for (var i=0; i<expressionArray.length; i++) {
			expression += expressionArray[i];
		}
		paretntDisplayObject.value = expression;
		paretntOriginalObject.value = expression;
		window.close();
	}

	function validateParantheses() {

		var error			= false;
		var paranthCount	= 0;
		var exprLength 		= expression.length;

		for (var i=0; i<exprLength; i++) {
			if (expression.charAt(i) == '(') {
				paranthCount++;
			}
			if (expression.charAt(i) == ')') {
				paranthCount--;
			}
			if (paranthCount<0) {
				return false;
			}
		}
		if (paranthCount == 0) {
			return true;
		}
		return false;

	}

	function addNumber(arg) {
		if (isFloat(arg)) {
			insertLit(arg);
		} else {
			alert('Enter a valid Number');
		}
	}

	function addConstant(arg, stringType) {
		if (! isSpace(arg)) {
			if (stringType.checked) {
				stringType.checked = false;
				insertLit('"' + arg + '"');
			} else {
				insertLit(arg);
			}
		} else {
			alert('Enter a Value');
		}
	}

        function eventChange() {
            document.exp.ActionType.value = "ACTIONPOPUP";
            document.exp.sourceType.value = "POLICYMESSAGE";
            document.exp.action = 'BuildServlet';       
            document.exp.wmsEventId.value = document.exp.selectedWMSEventId.options[document.exp.selectedWMSEventId.options.selectedIndex].value;
            document.exp.submit();
        }

