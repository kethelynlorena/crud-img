/* Pré Visualização da Imagem Escolhida */
function preViewImg() {
	var imgPreView = new FileReader();
	imgPreView.readAsDataURL(document.getElementById("uploadImage").files[0]);

	imgPreView.onload = function(imgPreViewEvent) {
		document.getElementById("preView").src = imgPreViewEvent.target.result;
	}
}

/* Pré Visualização das Imagens Escolhidas - UMA POR UMA */
var images = [];
function preViewerImages() {
	var imgPreView = new FileReader();
	imgPreView.readAsDataURL(document.getElementById("uploadImg").files[0]);

	imgPreView.onload = function (imgPreViewEvent) {
		images.push("<img src=" + imgPreViewEvent.target.result + " alt=\"...\"/>");
		document.getElementById("preViews").innerHTML = images;
	}
}

/* Pré Visualização das Imagens Escolhidas - TODAS */
function preViewerImages2() {
	const files = document.getElementById("uploadImg").files;	
	var imgPreView = [];

	for (var i = 0; i < files.length; i++) {
		imgPreView[i] = new FileReader();
		imgPreView[i].readAsDataURL(files[i]);

		imgPreView[i].onload = function (imgPreViewEvent) {
			images.push("<img src=" + imgPreViewEvent.target.result + " alt=\"...\"/>");
			document.getElementById("preViews").innerHTML = images;
		}
	}

}

function clearImages() {
	document.getElementById("preViews").innerHTML = null;
	images = [];
}

function removeLastImage() {
	images.pop();
	document.getElementById("preViews").innerHTML = images;
}

function removeFirstImage() {
	images.shift();
	document.getElementById("preViews").innerHTML = images;
}

function listarImagens() {
	images.forEach(function (item, indice) {
		console.log(indice + " -> "+ item);
		console.log("Files -> "+ document.getElementById("uploadImg").value);
	  });
}


function validaSenha() {
	var novaSenha= document.getElementById("inputNovaSenha");
	var confirmaNovaSenha= document.getElementById("inputConfirmaNovaSenha");
	
	if(novaSenha.value == confirmaNovaSenha.value) {
		document.getElementById("serverMessage").innerText = "";
		document.getElementById("btnAlterarSenha").disabled = false;
		
		return true;
	} else {
		document.getElementById("inputConfirmaNovaSenha").value = "";
		document.getElementById("serverMessage").innerText = "As senhas não conferem!!!";
		document.getElementById("btnAlterarSenha").disabled = true;
		
		return false;
	}
}
