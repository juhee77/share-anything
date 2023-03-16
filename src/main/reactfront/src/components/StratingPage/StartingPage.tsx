import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Layout from 'components/Layout/Layout';

const StartingPage = () => {

    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [postsPerPage, setPostsPerPage] = useState(10);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const response = await axios.get(
                "/post/all"
            );
            setPosts(response.data);
            setLoading(false);
        };
        fetchData();
    }, []);

    console.log(posts);

    return (
        <div>
            <h1>게시물 목록</h1>


            <main>
                {posts.map(({ id, postId, title, writer, view }) => (
                    <article key={postId}>
                        <h3>
                            {id} {title}/ {writer}/ {view}
                        </h3>
                    </article>
                ))}
            </main>
        </div>
    );
}

export default StartingPage;