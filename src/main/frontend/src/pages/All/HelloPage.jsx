import React from "react";
const HelloPage = () => {
    const name = '엘리';
    const list = ['우유', '딸기', '바나나','요거트'];
    return (
        <>
            <h1>Hello!</h1>
            <h2>{`Hello ${name}`}</h2>
            <ul>
                {
                    list.map((item) => <li>{item}</li>)
                }
            </ul>
            <img
                style={{width: '200px', height:'200px'}}
                src="https://plus.unsplash.com/premium_photo-1671014964764-4a279dbc6f68?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                alt="nature"/>
        </>
    )
}
export default HelloPage;