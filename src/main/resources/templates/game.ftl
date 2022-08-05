<html>
<body>
<h1>Game: ${game.id}</h1>

<#if game.gang??>
<h2>Gangname: ${game.gang.name}</h2>
<#else>
<form method="post" action="${game.id}/new-gang">
    <input type="text" name="yakTribeGangUrl"/>
    <input type="submit" name="Add your Gang"/>
</form>
</#if>


</body>
</html>
