let allFoods = [];
let selectedCategory = "All";
let selectedFoodPrice = 0;

// =====================================================
// LOAD FOODS FROM JAVA / MONGODB
// =====================================================

async function loadFoods() {

    const container = document.getElementById("foodContainer");

    if (!container) {
        console.error("foodContainer not found");
        return;
    }

    try {

        console.log("Loading foods from MongoDB...");

        const response = await fetch("/api/foods");

        console.log("Food API status:", response.status);

        if (!response.ok) {
            throw new Error("Server returned " + response.status);
        }

        const data = await response.json();

        console.log("Foods received:", data);

        allFoods = data;

        displayFoods(allFoods);

    } catch (error) {

        console.error("Food loading error:", error);

        container.innerHTML = `
            <div class="loading">
                ❌ Unable to load foods.
                <br><br>
                Please make sure:
                <br>
                1. MongoDB is running
                <br>
                2. Java server is running
                <br>
                3. Food data exists in MongoDB
            </div>
        `;
    }
}


// =====================================================
// DISPLAY FOODS
// =====================================================

function displayFoods(foods) {

    const container = document.getElementById("foodContainer");

    if (!container) {
        console.error("foodContainer not found");
        return;
    }

    container.innerHTML = "";

    if (!foods || foods.length === 0) {

        container.innerHTML = `
            <div class="loading">
                🍽️ No food items available.
            </div>
        `;

        return;
    }

    foods.forEach(food => {

        const card = document.createElement("div");

        card.className =
            "food-card" +
            (food.available ? "" : " unavailable");

        const emoji = getFoodEmoji(
            food.category,
            food.name
        );

        card.innerHTML = `

            <div class="food-image">
                ${emoji}
            </div>

            <div class="food-info">

                <div class="food-category">
                    ${escapeHTML(food.category)}
                </div>

                <div class="food-name">
                    ${escapeHTML(food.name)}
                </div>

                <div class="food-bottom">

                    <div class="food-price">
                        ₹${Number(food.price).toFixed(0)}
                    </div>

                    ${
                        food.available
                        ?
                        `
                        <button
                            class="order-btn"
                            onclick="openOrderModal(
                                '${escapeJS(food.name)}',
                                ${Number(food.price)}
                            )">
                            Order
                        </button>
                        `
                        :
                        `
                        <button
                            class="order-btn"
                            disabled>
                            Unavailable
                        </button>
                        `
                    }

                </div>

            </div>
        `;

        container.appendChild(card);
    });
}


// =====================================================
// FOOD EMOJI
// =====================================================

function getFoodEmoji(category, name) {

    const text =
        (
            String(category || "") +
            " " +
            String(name || "")
        ).toLowerCase();

    if (text.includes("biryani"))
        return "🍛";

    if (text.includes("pizza"))
        return "🍕";

    if (text.includes("burger"))
        return "🍔";

    if (text.includes("dosa"))
        return "🥞";

    if (text.includes("idli"))
        return "🥟";

    if (text.includes("noodle"))
        return "🍜";

    if (text.includes("rice"))
        return "🍚";

    if (text.includes("chicken"))
        return "🍗";

    if (text.includes("cake"))
        return "🍰";

    if (text.includes("ice cream"))
        return "🍨";

    if (text.includes("juice"))
        return "🧃";

    if (text.includes("coffee"))
        return "☕";

    if (text.includes("tea"))
        return "🍵";

    if (text.includes("dessert"))
        return "🍰";

    if (text.includes("beverage"))
        return "🥤";

    if (text.includes("south indian"))
        return "🥘";

    if (text.includes("fast food"))
        return "🍟";

    if (text.includes("main course"))
        return "🍽️";

    return "🍽️";
}


// =====================================================
// CATEGORY FILTER
// =====================================================

function filterCategory(category, button) {

    selectedCategory = category;

    document
        .querySelectorAll(".category-btn")
        .forEach(btn => {

            btn.classList.remove("active");

        });

    if (button) {
        button.classList.add("active");
    }

    applyFilters();
}


// =====================================================
// SEARCH
// =====================================================

function searchFoods() {

    applyFilters();
}


// =====================================================
// APPLY FILTERS
// =====================================================

function applyFilters() {

    const searchBox =
        document.getElementById("searchBox");

    const search =
        searchBox
        ? searchBox.value.toLowerCase().trim()
        : "";

    const filtered =
        allFoods.filter(food => {

            const category =
                String(food.category || "")
                    .toLowerCase();

            const name =
                String(food.name || "")
                    .toLowerCase();

            const matchesCategory =
                selectedCategory === "All" ||
                category ===
                selectedCategory.toLowerCase();

            const matchesSearch =
                name.includes(search) ||
                category.includes(search);

            return (
                matchesCategory &&
                matchesSearch
            );
        });

    displayFoods(filtered);
}


// =====================================================
// OPEN ORDER MODAL
// =====================================================

function openOrderModal(foodName, price) {

    console.log(
        "Opening order modal:",
        foodName,
        price
    );

    selectedFoodPrice = Number(price);

    const selectedFoodName =
        document.getElementById("selectedFoodName");

    const selectedFoodPriceElement =
        document.getElementById("selectedFoodPrice");

    const selectedFood =
        document.getElementById("selectedFood");

    const selectedPrice =
        document.getElementById("selectedPrice");

    const quantity =
        document.getElementById("quantity");

    const orderModal =
        document.getElementById("orderModal");

    if (!orderModal) {

        alert("Order modal not found in index.html");

        return;
    }

    if (selectedFoodName)
        selectedFoodName.textContent = foodName;

    if (selectedFoodPriceElement)
        selectedFoodPriceElement.textContent =
            selectedFoodPrice.toFixed(0);

    if (selectedFood)
        selectedFood.value = foodName;

    if (selectedPrice)
        selectedPrice.value = selectedFoodPrice;

    if (quantity)
        quantity.value = 1;

    updateTotal();

    orderModal.classList.add("show");
}


// =====================================================
// CLOSE ORDER MODAL
// =====================================================

function closeOrderModal() {

    const modal =
        document.getElementById("orderModal");

    if (modal) {
        modal.classList.remove("show");
    }
}


// =====================================================
// CHANGE QUANTITY
// =====================================================

function changeQuantity(amount) {

    const input =
        document.getElementById("quantity");

    if (!input) return;

    let quantity =
        parseInt(input.value) || 1;

    quantity += amount;

    if (quantity < 1) {
        quantity = 1;
    }

    input.value = quantity;

    updateTotal();
}


// =====================================================
// UPDATE TOTAL
// =====================================================

function updateTotal() {

    const quantityInput =
        document.getElementById("quantity");

    const totalElement =
        document.getElementById("orderTotal");

    if (!quantityInput || !totalElement)
        return;

    const quantity =
        parseInt(quantityInput.value) || 1;

    const total =
        selectedFoodPrice * quantity;

    totalElement.textContent =
        total.toFixed(0);
}


// =====================================================
// PLACE ORDER
// =====================================================

function setupOrderForm() {

    const orderForm =
        document.getElementById("orderForm");

    if (!orderForm) {

        console.error(
            "orderForm not found"
        );

        return;
    }

    orderForm.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();

            const name =
                document
                    .getElementById("customerName")
                    .value
                    .trim();

            const phone =
                document
                    .getElementById("phone")
                    .value
                    .trim();

            const email =
                document
                    .getElementById("email")
                    .value
                    .trim();

            const foodName =
                document
                    .getElementById("selectedFood")
                    .value;

            const price =
                Number(
                    document
                        .getElementById("selectedPrice")
                        .value
                );

            const quantity =
                parseInt(
                    document
                        .getElementById("quantity")
                        .value
                );

            const total =
                price * quantity;


            // -------------------------------
            // VALIDATION
            // -------------------------------

            if (
                !name ||
                !phone ||
                !email ||
                !foodName ||
                quantity < 1
            ) {

                alert(
                    "Please enter all details."
                );

                return;
            }


            try {

                console.log(
                    "Sending order to Java..."
                );

                // =================================================
                // IMPORTANT
                // Main.java /api/orders creates:
                // 1. Customer
                // 2. Order
                // =================================================

                const response =
                    await fetch(
                        "/api/orders",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify({

                                    customerName:
                                        name,

                                    phone:
                                        phone,

                                    email:
                                        email,

                                    foodName:
                                        foodName,

                                    quantity:
                                        quantity,

                                    total:
                                        total
                                })
                        }
                    );


                console.log(
                    "Order response:",
                    response.status
                );


                if (!response.ok) {

                    const errorText =
                        await response.text();

                    console.error(
                        errorText
                    );

                    throw new Error(
                        "Order failed"
                    );
                }


                const result =
                    await response.json();


                console.log(
                    "Order result:",
                    result
                );


                // =================================================
                // CLOSE ORDER MODAL
                // =================================================

                closeOrderModal();


                // =================================================
                // SUCCESS MESSAGE
                // =================================================

                const successOrderId =
                    document.getElementById(
                        "successOrderId"
                    );

                if (successOrderId) {

                    successOrderId.textContent =
                        result.orderId ||
                        "ORD-" + Date.now();
                }


                const successMessage =
                    document.getElementById(
                        "successMessage"
                    );

                if (successMessage) {

                    successMessage
                        .classList
                        .add("show");
                }


                // =================================================
                // RESET FORM
                // =================================================

                orderForm.reset();

                document
                    .getElementById("quantity")
                    .value = 1;


            } catch (error) {

                console.error(
                    "Order error:",
                    error
                );

                alert(
                    "Unable to place order.\n\n" +
                    "Please make sure MongoDB and " +
                    "the Java server are running."
                );
            }
        }
    );
}


// =====================================================
// CLOSE SUCCESS MESSAGE
// =====================================================

function closeSuccess() {

    const successMessage =
        document.getElementById(
            "successMessage"
        );

    if (successMessage) {

        successMessage
            .classList
            .remove("show");
    }
}


// =====================================================
// CLICK OUTSIDE ORDER MODAL
// =====================================================

function setupModal() {

    const modal =
        document.getElementById(
            "orderModal"
        );

    if (!modal) return;

    modal.addEventListener(
        "click",
        function(event) {

            if (event.target === modal) {

                closeOrderModal();
            }
        }
    );
}


// =====================================================
// ESCAPE HTML
// =====================================================

function escapeHTML(value) {

    return String(value || "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


// =====================================================
// ESCAPE JAVASCRIPT
// =====================================================

function escapeJS(value) {

    return String(value || "")
        .replace(/\\/g, "\\\\")
        .replace(/'/g, "\\'")
        .replace(/"/g, '\\"')
        .replace(/\r/g, "\\r")
        .replace(/\n/g, "\\n");
}


// =====================================================
// START APPLICATION
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function() {

        console.log(
            "Restaurant website loaded"
        );

        loadFoods();

        setupOrderForm();

        setupModal();
    }
);