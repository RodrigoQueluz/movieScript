<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form enctype="multipart/form-data" action="/avenuep/produtos" method="post">
    <div>
        <label>Título</label><br>
        <input type="text" name="titulo" />
    </div>
    <div>
        <label>Descrição</label><br>
        <textarea rows="10" cols="20" name="descricao"></textarea>
    </div>
    <div>
        <label>Páginas</label><br>
        <input type="text" name="paginas" />
    </div>
    <div>
			<label>Sumário</label><br>
			 <input name="file" type="file" />
		</div><br><br>
    <button type="submit">Cadastrar</button>
</form>
</body>
</html>