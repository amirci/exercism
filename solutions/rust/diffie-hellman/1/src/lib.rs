pub fn private_key(p: u64) -> u64 {
    let nanos = std::time::SystemTime::now()
        .duration_since(std::time::UNIX_EPOCH)
        .expect("system time should be after Unix epoch")
        .as_nanos() as u64;

    2 + nanos % (p - 2)
}

pub fn public_key(p: u64, g: u64, a: u64) -> u64 {
    modular_pow(g, a, p)
}

pub fn secret(p: u64, b_pub: u64, a: u64) -> u64 {
    modular_pow(b_pub, a, p)
}

fn modular_pow(base: u64, exponent: u64, modulus: u64) -> u64 {
    let mut result = 1;
    let mut base = base % modulus;
    let mut exponent = exponent;

    while exponent > 0 {
        if exponent % 2 == 1 {
            result = multiply_mod(result, base, modulus);
        }

        base = multiply_mod(base, base, modulus);
        exponent /= 2;
    }

    result
}

fn multiply_mod(left: u64, right: u64, modulus: u64) -> u64 {
    ((left as u128 * right as u128) % modulus as u128) as u64
}
