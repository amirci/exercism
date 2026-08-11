using PhoneNumberInfo = (bool IsNewYork, bool IsFake, string LocalNumber);

public static class PhoneNumber
{
    public static PhoneNumberInfo Analyze(string phoneNumber) =>
        (phoneNumber[..3] == "212", phoneNumber[4..7] == "555", phoneNumber[^4..]);


    public static bool IsFake(PhoneNumberInfo phoneNumberInfo) => phoneNumberInfo.IsFake;
}
