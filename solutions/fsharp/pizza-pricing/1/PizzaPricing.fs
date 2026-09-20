module PizzaPricing

type Pizza =
    | Margherita
    | Caprese
    | Formaggio
    | ExtraSauce of Pizza
    | ExtraToppings of Pizza

let rec pizzaPrice pizza =
    match pizza with
    | Margherita -> 7
    | Caprese -> 9
    | Formaggio -> 10
    | ExtraSauce pizza -> pizzaPrice pizza + 1
    | ExtraToppings pizza -> pizzaPrice pizza + 2

let orderPrice pizzas =
    let rec total price = function
        | [] -> price
        | pizza :: remaining -> total (price + pizzaPrice pizza) remaining

    let orderFee =
        match pizzas with
        | [ _ ] -> 3
        | [ _; _ ] -> 2
        | _ -> 0

    total orderFee pizzas
