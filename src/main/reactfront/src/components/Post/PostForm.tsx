import React, { useContext, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../../store/auth-context';


const PostForm = () => {

    let navigate = useNavigate();
    const authCtx = useContext(AuthContext);
    const titleInputRef = useRef<HTMLInputElement>(null);
    const textInputRef = useRef<HTMLTextAreaElement>(null);
    const publicInputRef = useRef<HTMLInputElement>(null);
    const boardInputRef = useRef<HTMLInputElement>(null);

    const submitHandler = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const enteredTitleInputRef = titleInputRef.current!.value;
        const enteredTextInputRef = textInputRef.current!.value;
        const enteredPublicInputRef = publicInputRef.current!.checked;
        const enteredBoardInputRef = boardInputRef.current!.value;

        console.log("postSubmit");
        authCtx.addPost(enteredTitleInputRef, enteredTextInputRef, enteredPublicInputRef, enteredBoardInputRef);
        if (authCtx.isSuccess) {
            alert("포스트 게시 완료")
            navigate("/");
        }
    };

    return (

        <form onSubmit={submitHandler} className="form-horizontal">
            <div>
                <label htmlFor="title" className="col-sm-2 control-label">Title</label>
                <input
                    id="title"
                    type="text"
                    ref={titleInputRef}
                    className="form-control"
                />
            </div>
            <div>
                <label htmlFor="text" className="col-sm-2 control-label">Text</label>
                <textarea
                    id="text"
                    ref={textInputRef}
                    className="form-control"
                />
            </div>
            <div>
                <label htmlFor="isPublic" className="col-sm-2 control-label">Is Public</label>
                <input
                    id="isPublic"
                    type="checkbox"
                    ref={publicInputRef}
                />
            </div>
            <div>
                <label htmlFor="board" className="col-sm-2 control-label">Board</label>
                <input
                    id="board"
                    ref={boardInputRef} className="form-control"
                />
            </div>
            <button type="submit" className="btn btn-primary btn">Submit</button>
        </form>
    );
}

export default PostForm;
