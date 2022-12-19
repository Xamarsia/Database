public class Dish {
    private String dishName;
    private float cost;
    private float weight;
    private Integer discountPercent;

    public Dish(String dishName, float cost, float weight, Integer discountPercent) {
        this.setDishName(dishName);
        this.setCost(cost);
        this.setWeight(weight);
        this.setDiscountPercent(discountPercent);
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }


    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }
}
