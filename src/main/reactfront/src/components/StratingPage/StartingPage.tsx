import axios from 'axios';
import { useEffect, useState } from 'react';
import './StartingPage.css';

interface Post {
    id: number;
    title: string;
    writer: string;
    view: number;
    board: string;
}

const StartingPage = () => {

    const [posts, setPosts] = useState<Post[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [postsPerPage, setPostsPerPage] = useState(10);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const response = await axios.get<Post[]>('/post');
            setPosts(response.data);
            setLoading(false);
        };
        fetchData();
    }, []);

    console.log(posts);

    return (
        <div className='form-wrapper'>
            {loading ? (
                <div>Loading...</div>
            ) : (
                <table className="table">
                    <thead>
                        <tr>
                            <th>번호</th>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>조회수</th>
                        </tr>
                    </thead>
                    <tbody>
                        {posts.map((post, index) => (
                            <tr key={post.id}>
                                <td>{index + 1}</td>
                                <td>{post.title}</td>
                                <td>{post.writer}</td>
                                <td>{post.view}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default StartingPage;