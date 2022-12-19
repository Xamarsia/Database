public class Main {

    static public void setMenuData(MenuDatabase menuDatabase) {
        menuDatabase.insertDish(new Dish("Fish curry", 125, 200, 0));
        menuDatabase.insertDish(new Dish("Sushi", 345, 100, 10));
        menuDatabase.insertDish(new Dish("Ramen", 225, 80, 5));
        menuDatabase.insertDish(new Dish("Tom Yam Goong", 125, 130, 20));
        menuDatabase.insertDish(new Dish("Kebab", 50, 150, 0));

    }

    static public void task1Solution() {
        MenuDatabase menuDatabase = new MenuDatabase();
        menuDatabase.createTable();
        setMenuData(menuDatabase);
        menuDatabase.findDishesByCost(60, 300);
        menuDatabase.findDishesWithDiscount();
    }

    public static void main(String[] args)  {
        task1Solution();
    }
}