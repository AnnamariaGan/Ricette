package com.example.ricette.data.local

data class SeedItem(val id: Long, val name: String, val kcal: Double, val carbs: Double, val prot: Double, val fat: Double, val df: Boolean, val gf: Boolean, val unit: String = "g", val gPerUnit: Double = 1.0)
data class SeedRecipe(val id: Long, val title: String, val cat: String, val serv: Int, val min: Int, val steps: String, val notes: String)
data class SeedIng(val rId: Long, val fId: Long, val g: Double)

object InitialSeedData {
    val items = listOf(
        SeedItem(1,"Farina 00",340.0,72.0,10.0,1.0,true,false), SeedItem(2,"Semola di grano duro",345.0,71.0,11.5,1.2,true,false),
        SeedItem(3,"Farina Pizzeria Caputo",352.0,71.0,12.5,1.0,true,false), SeedItem(4,"Fecola / Amido",345.0,83.0,0.5,0.1,true,true),
        SeedItem(5,"Acqua",0.0,0.0,0.0,0.0,true,true), SeedItem(6,"Lievito di birra fresco",105.0,18.0,12.0,1.9,true,true),
        SeedItem(7,"Lievito di birra secco",325.0,41.0,40.0,7.6,true,true), SeedItem(8,"Lievito per dolci",120.0,28.0,0.5,0.0,true,true),
        SeedItem(9,"Sale fino",0.0,0.0,0.0,0.0,true,true), SeedItem(10,"Olio EVO",899.0,0.0,0.0,99.9,true,true,"cucchiai",10.0),
        SeedItem(11,"Uova",143.0,0.7,12.6,9.5,true,true,"uova",60.0), SeedItem(12,"Burro",717.0,0.1,0.8,81.0,false,true),
        SeedItem(13,"Zucchero",392.0,100.0,0.0,0.0,true,true), SeedItem(14,"Cacao amaro in polvere",355.0,18.0,20.0,21.0,true,true),
        SeedItem(15,"Orzo solubile",360.0,80.0,5.5,0.5,true,false), SeedItem(16,"Latte intero",64.0,4.9,3.3,3.6,false,true),
        SeedItem(17,"Semi di anice",337.0,50.0,18.0,16.0,true,true), SeedItem(18,"Vanillina e scorza di limone",290.0,70.0,0.2,0.1,true,true),
        SeedItem(19,"Cioccolato fondente",546.0,46.0,5.0,31.0,true,true), SeedItem(20,"Mele",52.0,14.0,0.3,0.2,true,true,"mele",150.0),
        SeedItem(21,"Savoiardi",380.0,75.0,8.5,4.0,true,false), SeedItem(22,"Mascarpone",453.0,2.5,4.8,47.0,false,true),
        SeedItem(23,"Caffè espresso",2.0,0.0,0.1,0.0,true,true), SeedItem(24,"Riso per arancine",350.0,78.0,6.7,0.6,true,true),
        SeedItem(25,"Parmigiano Reggiano",392.0,0.0,33.0,28.0,false,true), SeedItem(26,"Zafferano e dado",250.0,30.0,10.0,8.0,true,true)
    )
    val recipes = listOf(
        SeedRecipe(1,"Torta Cacao e Orzo","Pasticceria",8,60,"1. Montare a neve gli albumi.\n2. Montare i rossi con zucchero e burro.\n3. Aggiungere farina 00, cacao, orzo e lievito.\n4. Unire l'acqua.\n5. Incorporare gli albumi a neve.\n6. Cuocere a 160°C per 45 minuti (o anche di più).","50 g totali tra cacao e orzo (10-15 g orzo)."),
        SeedRecipe(2,"Torta al Cacao","Pasticceria",8,50,"1. Montare 3 uova con lo zucchero.\n2. Aggiungere burro e latte.\n3. Aggiungere farina 00, cacao, lievito e un pizzico di sale.\n4. Cuocere a 180°C per 30-35 minuti.","1 bustina di lievito e 1 pizzico di sale."),
        SeedRecipe(3,"Mufuletti di San Martino","Panificazione",4,150,"1. Impastare semola, acqua, lievito, olio, anice e sale.\n2. Far lievitare.\n3. Formare i panini da circa 200 g ciascuno.\n4. Far lievitare ancora.\n5. Infornare a 180/200°C per 20-25 minuti.","4 cucchiai olio, 1 bustina lievito secco o di birra, 20 g semi di anice."),
        SeedRecipe(4,"Pane Bianco Veloce","Panificazione",6,120,"1. Sciogliere il lievito in un poco d'acqua.\n2. Mettere farina in una ciotola, fare un buco e versare lievito e resto acqua.\n3. Mescolare con un cucchiaio, aggiungere sale e mescolare (impasto granuloso e appiccicoso).\n4. Spolverare con farina e far lievitare 1h 30m.\n5. Mettere in teglia con carta forno, fare le divisioni e cuocere a 230°C per 30 minuti.","2 cucchiaini di sale."),
        SeedRecipe(5,"Pan di Spagna (23x33 cm)","Pasticceria",12,55,"1. Montare uova e zucchero.\n2. Setacciare farina 00 e fecola e inserirle a poco a poco dal basso verso l'alto con lievito, vanillina e scorza di limone.\n3. Cuocere a 180°C per 40 minuti (o anche più).","Teglia 24 cm: 5 uova, 150 g farina, 30 g fecola, 180 g zucchero, 1 lievito, 1 vanillina, 1 limone.\nTeglia 28 cm: 7 uova, 205 g farina, 40 g fecola, 245 g zucchero, 1 lievito, 1.5 vanillina, 1 limone."),
        SeedRecipe(6,"Focaccia Barese (30/32 cm)","Panificazione",6,110,"1. Mescolare farina setacciata e sale.\n2. Sciogliere il lievito nell'acqua tiepida e versare sulla farina.\n3. Impastare con forchetta o cucchiaio.\n4. Oliare una ciotola, versare l'impasto e fare le pieghe 2-3 volte.\n5. Oliare la teglia, stendere dopo 10 min di riposo, aspettare altri 10 min e condire.\n6. Cuocere a 250°C per 25 minuti circa.","Condimento: pomodorini, cipolle, patate."),
        SeedRecipe(7,"Pizza in Teglia","Panificazione",4,240,"1. Impastare farina Caputo e metà acqua (impasto grezzo), frigo 30 min.\n2. Inserire lievito con acqua rimanente a poco a poco e impastare.\n3. Inserire sale e impastare.\n4. Lasciare in frigo con olio nei bordi per qualche ora.\n5. Fare pieghe e far lievitare.\n6. Mettere in teglia, lievitare 1 ora, infornare sul basso a temperatura alta per 30 min.","3-4 g lievito di birra fresco."),
        SeedRecipe(8,"Pancake (10 pezzi)","Pasticceria",10,20,"1. Montare zucchero e uova.\n2. Aggiungere lievito e poi a poco a poco farina 00 e acqua.\n3. Cuocere in padellino unto a fuoco basso, girando dopo che le bollicine sono scoppiate.","Dose per circa 10 pancake grandi."),
        SeedRecipe(9,"Brownies","Pasticceria",8,40,"1. Sciogliere cioccolato fondente a bagnomaria e unirlo al burro fuso con frusta.\n2. A parte montare uova, zucchero e sale.\n3. Aggiungere farina, cacao, cioccolato e burro fusi.\n4. Versare in teglia e cuocere a 180°C per 25 minuti.","Teglia 21x28 cm: 500 g cioccolato, 320 g zucchero, 4 uova, 40 g cacao, 340 g burro, 180 g farina + lievito."),
        SeedRecipe(10,"Torta di Mele","Pasticceria",8,65,"1. Montare zucchero e uova con burro.\n2. Aggiungere vanillina, scorza di limone e lievito.\n3. Incorporare a poco a poco la farina 00.\n4. Versare in teglia, inserire le mele tagliate e infornare 50 minuti a minimo.","Variante: 250 g amido + 50 g farina; zucchero 300-350 g."),
        SeedRecipe(11,"Panzerotti","Panificazione",10,150,"1. Impastare farina 0, lievito, olio, sale e acqua e far lievitare.\n2. Formare panetti da 50-60 g e far lievitare ancora.\n3. Farcire con pomodoro, mozzarella e origano, chiudere bene i bordi.\n4. Friggere in olio non troppo caldo.","20 g lievito di birra, 4 cucchiai olio, 1 cucchiaino sale."),
        SeedRecipe(12,"Tiramisù (Teglia Media)","Pasticceria",10,35,"1. Montare tuorli e zucchero.\n2. Montare albumi a neve.\n3. Unire mascarpone ai tuorli e poi incorporare gli albumi.\n4. Comporre con savoiardi inzuppati nel caffè e cacao amaro.","1 pacco savoiardi, 1 kg mascarpone, 8 uova, 4 cucchiai zucchero."),
        SeedRecipe(13,"Crema Pasticcera","Pasticceria",6,20,"1. Mettere in freezer una ciotola di vetro.\n2. Portare a bollore a fuoco basso latte con vaniglia e scorza di limone.\n3. Mescolare a parte tuorli, zucchero e amido senza grumi.\n4. Versare il latte bollente in 3 volte filtrandolo, poi addensare sul fuoco basso girando con frusta.\n5. Versare nella ciotola fredda.","500 g latte intero, 7 tuorli, 130 g zucchero, 40 g amido."),
        SeedRecipe(14,"Arancine (1 kg di riso)","Primi Piatti",16,120,"1. Cuocere al dente il riso in 2 L acqua con 4 dadi e scolarlo.\n2. Unire burro, parmigiano, tuorli, zafferano e sugo del ripieno, poi far raffreddare.\n3. Formare le arancine farcendole al centro.\n4. Passare nella pastella (o albume) e nel pangrattato, quindi friggere.","Ragù: ~600 g carne macinata, 200 g piselli, 1.5 tubetto concentrato.\nBurro: 600 g prosciutto cotto, 600 g mozzarella scolata il giorno prima, 500 g besciamella.")
    )
    val ings = listOf(
        SeedIng(1,11,240.0),SeedIng(1,12,125.0),SeedIng(1,14,38.0),SeedIng(1,15,12.0),SeedIng(1,5,100.0),SeedIng(1,13,200.0),SeedIng(1,1,250.0),SeedIng(1,8,16.0),
        SeedIng(2,11,180.0),SeedIng(2,13,180.0),SeedIng(2,1,200.0),SeedIng(2,12,150.0),SeedIng(2,16,100.0),SeedIng(2,14,40.0),SeedIng(2,8,16.0),SeedIng(2,9,2.0),
        SeedIng(3,2,500.0),SeedIng(3,5,340.0),SeedIng(3,7,7.0),SeedIng(3,9,10.0),SeedIng(3,10,40.0),SeedIng(3,17,20.0),
        SeedIng(4,1,500.0),SeedIng(4,5,370.0),SeedIng(4,6,12.0),SeedIng(4,9,10.0),
        SeedIng(5,11,480.0),SeedIng(5,1,250.0),SeedIng(5,4,50.0),SeedIng(5,13,300.0),SeedIng(5,18,5.0),SeedIng(5,8,16.0),
        SeedIng(6,1,300.0),SeedIng(6,5,220.0),SeedIng(6,9,6.0),SeedIng(6,6,3.0),SeedIng(6,10,30.0),
        SeedIng(7,3,400.0),SeedIng(7,5,280.0),SeedIng(7,9,9.0),SeedIng(7,6,4.0),
        SeedIng(8,11,180.0),SeedIng(8,13,150.0),SeedIng(8,8,16.0),SeedIng(8,1,300.0),SeedIng(8,5,300.0),
        SeedIng(9,19,260.0),SeedIng(9,13,160.0),SeedIng(9,11,120.0),SeedIng(9,14,20.0),SeedIng(9,12,170.0),SeedIng(9,1,90.0),SeedIng(9,9,2.0),
        SeedIng(10,1,300.0),SeedIng(10,13,350.0),SeedIng(10,12,150.0),SeedIng(10,11,240.0),SeedIng(10,18,10.0),SeedIng(10,8,16.0),SeedIng(10,20,600.0),
        SeedIng(11,1,400.0),SeedIng(11,6,20.0),SeedIng(11,10,40.0),SeedIng(11,9,6.0),SeedIng(11,5,250.0),
        SeedIng(12,21,400.0),SeedIng(12,22,1000.0),SeedIng(12,11,480.0),SeedIng(12,13,60.0),SeedIng(12,14,20.0),SeedIng(12,23,250.0),
        SeedIng(13,16,500.0),SeedIng(13,11,140.0),SeedIng(13,13,130.0),SeedIng(13,4,40.0),SeedIng(13,18,8.0),
        SeedIng(14,24,1000.0),SeedIng(14,5,2000.0),SeedIng(14,11,60.0),SeedIng(14,25,100.0),SeedIng(14,12,100.0),SeedIng(14,26,40.0)
    )
}
