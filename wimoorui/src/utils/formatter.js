const isBigNumber = num => !Number.isSafeInteger(+num)
// function that enquotes *big numbers* matching
// desired criteria into double quotes inside
// JSON string
//
// (function checking for *big numbers* may be
// passed as a second parameter for flexibility)
const enquoteBigNumber = (jsonString, bigNumChecker) =>
    jsonString
        .replaceAll(
            /([:\s\[,]*)(\d+)([\s,\]]*)/g,
            (matchingSubstr, prefix, bigNum, suffix) =>
                bigNumChecker(bigNum)
                    ? `${prefix}"${bigNum}"${suffix}`
                    : matchingSubstr
        )
// parser that turns matching *big numbers* in
// source JSON string to bigint
const parseWithBigInt = (jsonString, bigNumChecker) =>
    JSON.parse(
        enquoteBigNumber(jsonString, bigNumChecker),
        (key, value) =>
            !isNaN(value) && bigNumChecker(value)
                ? BigInt(value)
                : value
    )

function parseJSONWithBigInt(jsonstr){
	return parseWithBigInt(jsonstr, isBigNumber);
}
export default{
	 parseJSONWithBigInt
}