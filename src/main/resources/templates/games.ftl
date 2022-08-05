<html>
<body>
<form method="post">
    <input type="submit"/>
</form>

<h1>Items:</h1>
<#list games as game>
    <h2>The Game with ID ${game.id}</h2>
</#list>
</body>
</html>
